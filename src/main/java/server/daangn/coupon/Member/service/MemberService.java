package server.daangn.coupon.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.exception.MemberNotFoundException;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 멤버 보유 쿠폰 조회
    @Transactional
    public List<CouponResponseDto> readMemberCoupon(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return CouponResponseDto.toDtoList(
                new ArrayList<>(member.getCouponMember())
        );
    }
}
