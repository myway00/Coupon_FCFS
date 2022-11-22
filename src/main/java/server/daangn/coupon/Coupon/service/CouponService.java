package server.daangn.coupon.Coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponMemberNotFoundException;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Coupon.exception.CouponRunOutException;
import server.daangn.coupon.Coupon.repository.CouponMemberRepository;
import server.daangn.coupon.Coupon.repository.CouponRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.exception.MemberNotFoundException;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.List;

import static server.daangn.coupon.Coupon.service.SchedulerService.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final CouponMemberRepository couponMemberRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final CouponEventService couponEventService;

    /**
     * 쿠폰 발급 요청
     *
     * @param req
     * @return 쿠폰 발급 성공 시 : 쿠폰 응답 / 실패 시 : 쿠폰 발급 실패 응답
     *
     * 1) 사용자가 이전에 발급받은 쿠폰이 아니고 (중복 체킹 방지)
     *
     * 2) addQueue 를 통해 대기줄에 넣어두고 기다리기
     * 2-1 ) 쿠폰 발급 성공
     * - (addCoupon을 통해 쿠폰 추가 & DB 에 쿠폰 추가)
     * - 새로 발급된 쿠폰에 대한 정보 반환
     *
     * 2-2 ) 쿠폰 발급 실패
     * - 쿠폰 발급 실패 EXCEPTION 응답 주기
     *
     * (+) => 쿠폰 발급 성공 여부 아는법 !? :
     * 선착순 이벤트가 진행 중이면 계속해서 내가 쿠폰 발급 성공 리스트(PUBLISH LIST)에 있는지 여부를 통해 알기 가능
     * 선착순 이벤트가 끝날 때까지 PUBLISH LIST 에 없었다면 선착순 발급 실패한 것
     *
     * @throws InterruptedException - THREAD 로 DB에 쿠폰 생성될 때까지 기다려야 하는 이슈 존재
     *
     */
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto req) throws InterruptedException {

        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Coupon coupon = couponRepository.findByCouponType(CouponType.valueOf(req.getCouponCode())).orElseThrow(CouponNotFoundException::new);
        checkDuplicateCoupon(member, coupon);

        // 쿠폰 대기열에 추가
        couponEventService.addQueue(CouponType.valueOf(req.getCouponCode()), req.getMemberId());

        // (1) 신청한 쿠폰의 선착순 진행 이벤트가 현재 진행 중일 때
        while(isInProgress(coupon.getCouponType())){
            // (1-1) 내가 신청한 쿠폰 발급 성공자 리스트에 내가 들어가있다면 쿠폰 발급 성공 => 사용자에게 쿠폰 발급 성공 응답 반환하기
            if(couponEventService.isMemberInPublishSuccessList(coupon.getCouponType(), req.getMemberId())){
                Thread.sleep(1000); // DB 에 쿠폰이 추가될 지연 시간 1초
                 return CouponResponseDto.toDto(
                        couponMemberRepository.findByMemberAndCoupon(member,coupon)
                                .orElseThrow(CouponMemberNotFoundException::new)
                );
            }
        }
        // (2) 모든 선착순 진행 이벤트 종료 && 내가 publish list 에서 발견되지 않았을 경우
        throw new CouponRunOutException();
    }

    /**
     * 쿠폰 중복 발급 검사
     * @param member
     * @param coupon
     * 중복이라면 쿠폰 중복 에러 반환 , 아니라면 발급 과정 진행 이어서
     */
    boolean checkDuplicateCoupon(Member member, Coupon coupon){
        if(couponMemberRepository.findByMemberAndCoupon(member, coupon).isPresent()){
            throw new CouponDuplicateException();
        }
        return true;
    }

    /**
     * 쿠폰 사용
     * @param req
     * @return
     */
    @Transactional
    public CouponResponseDto useCoupon(CouponRequestDto req) {

        CouponMember couponMember = couponMemberRepository.findById(req.getCouponId()).orElseThrow(CouponMemberNotFoundException::new);
        couponMember.useCoupon(); // coupon 의 update time 을 갱신

        return CouponResponseDto.toDto(
                couponMember
        );
    }

    /**
     * 쿠폰 조회
     * @return CouponResponseDto(내가 지정한 쿠폰)
     */
    @Transactional
    public CouponResponseDto readCoupon(Long couponId) {
        return CouponResponseDto.toDto(
                couponMemberRepository.findById(couponId).orElseThrow(CouponMemberNotFoundException::new)
        );
    }

    // 전체 쿠폰 조회
    // TODO : 동시성, 선착순 이벤트 구현 완료 후 남은 쿠폰 수, 발급 쿠폰 수에 대한 정보 포함해서 응답값에 추가
    @Transactional
    public List<CouponResponseDto> readAllCoupon() {
        return CouponResponseDto.toDtoList(
                couponMemberRepository.findAll()
        );
    }

}
