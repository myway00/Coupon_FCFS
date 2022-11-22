package server.daangn.coupon.Coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.entity.couponEvent.CouponCount;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Coupon.repository.CouponRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.exception.MemberNotFoundException;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.HashMap;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CouponEventService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private static final long PUBLISH_SIZE = 100; // 한번에 처리해 줄 대기하는 사람의 수
    private static final long LAST_INDEX = 1;

    private HashMap<String , CouponCount> couponCounts = new HashMap<>();

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    /**
     * 쿠폰의 제한 갯수 초기화 ( 쿠폰 타입에 따라서 결정 )
     * @param couponType
     */
    public void setCouponCount(CouponType couponType) {
        this.couponCounts.put(couponType.couponName(), new CouponCount(couponType));
    }

    /**
     * 대기열에 추가
     * @param couponType
     * @param memberId
     */
    public void addQueue(CouponType couponType, Long memberId) {

        final String people = String.valueOf(memberId); // value : member id
        final long now = System.currentTimeMillis(); // score : register time

        redisTemplate.opsForZSet().add(String.valueOf(couponType), people, (int) now);
        log.info("대기열 - {} ({}초)", people, now);

    }

    /**
     * 멤버의 대기열 순서 NOTICE
     * @param couponType
     */
    public void getOrder(CouponType couponType) {

        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<Object> queue = redisTemplate.opsForZSet().range(String.valueOf(couponType), start, end);

        for (Object people : queue) {
            if (Long.parseLong(String.valueOf(Long.parseLong(people.toString()))) > 0L) {
                Long rank = redisTemplate.opsForZSet().rank(String.valueOf(couponType), people);
                log.info("'{}'멤버의 앞에는 {}명이 줄서있습니다.", people, rank);
            }
        }
    }

    /**
     * 대기열에 있는 사람들 중 대기열 RANK가 높은 순으로 100명에게 쿠폰 발급 & 대기열에서 REMOVE
     * @param couponType
     */
    @Transactional
    public void publish(CouponType couponType){

        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        Set<Object> queue = redisTemplate.opsForZSet().range(String.valueOf(couponType), start, end);
        for (Object people : queue) {

            if (validValue(people, couponType)) {
                // 쿠폰을 멤버의 쿠폰함에 추가 => 쿠폰 발급
                Coupon coupon = couponRepository.findByCouponType(couponType).orElseThrow(CouponNotFoundException::new);
                Member member = memberRepository.findById(Long.parseLong(people.toString())).orElseThrow(MemberNotFoundException::new);
                final CouponMember newCoupon = new CouponMember(member, coupon);
                member.addCoupon(newCoupon);

                log.info("'{}'멤버 {} 쿠폰 발급 ({})({})", people, newCoupon.getCoupon().getCouponType().couponName(), newCoupon.getCoupon().getCouponType(), this.couponCounts.get(couponType.couponName()).getLimit());

                redisTemplate.opsForZSet().remove(String.valueOf(couponType), people); // 기존 대기큐에서 remove

                // 쿠폰 발급 성공 hash  (publish list)  에 <couponType(code), memberId> put
                redisTemplate.opsForHash().putIfAbsent(String.valueOf(couponType.couponName()), people, "success");

                this.couponCounts.get(couponType.couponName()).decrease();

            }
        }
    }

    /**
     * 쿠폰을 다 발급해서 0개가 된다면 true 반환
     * @return 쿠폰을 다 발급 : true / 발급할 쿠폰 남아있음 : false
     */
    public boolean validEnd(CouponType couponType) {

        return (this.couponCounts.get(couponType.couponName())!= null && this.couponCounts.get(couponType.couponName()).end());
    }

    /**
     * redis 대기열에 남아있는 사람들의 수 반환
     * @param couponType
     * @return 대기열에 남아있는 사람들의 수
     */
    public long getWaitSize(CouponType couponType) {
        return redisTemplate.opsForZSet().size(String.valueOf(couponType));
    }

    /**
     * 발급할 수 있는 남은 쿠폰의 수
     * @param couponType
     * @return 남은 쿠폰의 수
     */
    public long getLeftCouponSize(CouponType couponType){
        return couponCounts.get(couponType.couponName()).getLimit() ;
    }

    /**
     * 멤버가 coupon publish 성공 리스트에 있는지 확인
     * @param couponType
     * @param memberId
     * @return 쿠폰 발급 성공 리스트에 멤버 존재 : true , 존재 x : false
     */
    public boolean isMemberInPublishSuccessList(CouponType couponType, Long memberId){
        return redisTemplate.opsForHash().get(String.valueOf(String.valueOf(couponType.couponName())), memberId.toString())!=null;
    }

    /**
     * coupon type 에 따른 쿠폰 발급 현황
     * @param couponType
     * @return 쿠폰 발급 멤버의 수
     */
    public Long getPublishSuccessListSize(CouponType couponType) {
        return redisTemplate.opsForHash().size(String.valueOf(couponType.couponName()));
    }

    /**
     * redis에 남아있는 대기열 데이터 초기화
     * @param couponType
     * @return
     */
    void initRedisQueue(CouponType couponType) {
                redisTemplate.delete(String.valueOf(couponType));
        }

    /**
     * redis에 남아있는 발급 성공 멤버 데이터 초기화
     * @param couponType
     * @return
     */
    void initPublishList(CouponType couponType) {
        redisTemplate.delete(String.valueOf(couponType.couponName()));
    }

    /**
     * people 값이 valid 한 값인지 검사
     * (1) 아이디가 유효하지 않은 멤버 여부
     * (2) 발급한 쿠폰 수가 쿠폰 한정 수량과 같거나 보다 크면 쿠폰 발급
     * (3) 유효한 멤버이면서 현재 쿠폰 타입의 남은 수량이 1장 이상일 때
     * @param people
     * @return
     */
    boolean validValue(Object people, CouponType couponType){
        if (Long.parseLong(String.valueOf(Long.parseLong(people.toString()))) <= 0L) return false;
        if (getPublishSuccessListSize(couponType)>=couponType.number())return false;
        memberRepository.findById(Long.parseLong(people.toString()));
        return memberRepository.findById(Long.parseLong(people.toString())).isPresent() && this.couponCounts.get(couponType.couponName()).getLimit() >= 1;
    }

}
