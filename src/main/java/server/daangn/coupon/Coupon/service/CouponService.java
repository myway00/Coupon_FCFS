package server.daangn.coupon.Coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMemberId;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponMemberNotFoundException;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Coupon.repository.CouponMemberRepository;
import server.daangn.coupon.Coupon.repository.CouponRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.exception.MemberNotFoundException;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final CouponMemberRepository couponMemberRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    // 쿠폰 발급
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto req) {

        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Coupon coupon = couponRepository.findByCouponType(CouponType.valueOf(req.getCouponCode())).orElseThrow(CouponMemberNotFoundException::new);
        checkDuplicateCoupon(member, coupon);
        return CouponResponseDto.toDto(
                couponMemberRepository.findByMemberAndCoupon(member,coupon)
                        .orElseThrow(CouponNotFoundException::new)
        );

    }

    // 쿠폰 중복 발급 검사
    void checkDuplicateCoupon(Member member, Coupon coupon){

        if(couponMemberRepository.findByMemberAndCoupon(member, coupon).isEmpty()){
            member.addCoupon(new CouponMember(member, coupon));
        }else{
            throw new CouponDuplicateException();
        }

    }

    // 쿠폰 사용
    @Transactional
    public CouponResponseDto useCoupon(CouponRequestDto req) {

        CouponMember couponMember = couponMemberRepository.findById(req.getCouponId()).orElseThrow(CouponMemberNotFoundException::new);
        couponMember.useCoupon(); // coupon 의 update time 을 갱신

        return CouponResponseDto.toDto(
                couponMember
        );
    }

    // 쿠폰 조회
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
