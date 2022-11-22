package server.daangn.coupon.Coupon.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.repository.CouponMemberRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CouponEventServiceTest {

    @Autowired private CouponEventService couponEventService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CouponMemberRepository couponMemberRepository;

    private static Long key = 0L;

    @Test
    void C0001_CouponEvent_10000명_중_선착순_1000명_쿠폰발급() throws InterruptedException {
        // given
        couponEventService.initRedisQueue(CouponType.C0001);
        // when, then
        FCFSCouponTest(CouponType.C0001, 10000, new CountDownLatch(10000), 50000);
    }

    @Test
    void E0001_CouponEvent_10000명_중_선착순_300명_쿠폰발급() throws InterruptedException {
        // given
        couponEventService.initRedisQueue(CouponType.E0001);
        // when, then
        FCFSCouponTest(CouponType.E0001, 10000, new CountDownLatch(10000), 15000);
    }

    private void FCFSCouponTest(CouponType couponType, int people, CountDownLatch countDownLatch, int testTime) throws InterruptedException {

        // 기존 총 쿠폰의 갯수
        int beforeCouponSize = couponMemberRepository.findAll().size();

        // 멤버 생성
        for(int i = 0; i < people ; i++){
            Member member = memberRepository.save(createMember());
        }

        // PARAMETER 로 넘어온 PEOPLE 수만큼 쓰레드 생성
        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker( countDownLatch, couponType )))
                .limit(people)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await();

        // 쿠폰 발급 과정동안 기다릴 시간
        Thread.sleep(testTime);

        // 쿠폰 선착순 갯수 - 남은 쿠폰의 갯수 == 쿠폰 새로 발급 갯수 == 현재 쿠폰 발급 성공 갯수
        assertEquals(couponType.number() - couponEventService.getLeftCouponSize(couponType), couponEventService.getPublishSuccessListSize(couponType));

        // 쿠폰 선착순 이벤트 전 쿠폰 발급 갯수 + 쿠폰 새로 발급 갯수 == 현재 쿠폰 발급 성공 갯수
        assertEquals(beforeCouponSize+couponEventService.getPublishSuccessListSize(couponType), couponMemberRepository.findAll().size());
    }

    private class AddQueueWorker implements Runnable{

        private CountDownLatch countDownLatch;
        private CouponType event;
        private Long memberKey ;

        public AddQueueWorker(CountDownLatch countDownLatch, CouponType event) {
            this.countDownLatch = countDownLatch;
            this.event = event;
            this.memberKey=++key; //멤버 아이디 1~지정된 멤버 수
        }

        @Override
        public void run() {
            couponEventService.addQueue(event, this.memberKey);
            countDownLatch.countDown();
        }
    }

    public Member createMember() {
        return new Member();
    }

}
