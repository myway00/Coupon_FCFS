package server.daangn.coupon.Member.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks MemberService memberService;
    @Mock MemberRepository memberRepository;

    @Test
    void readMemberCouponTest(){

        // given
        Member member = memberRepository.save(createMember());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        // when
        List<CouponResponseDto> couponResponseDto = memberService.readMemberCoupon(anyLong());

        //then
        assertThat(couponResponseDto.size()).isEqualTo(0);

    }

    public Coupon createCoupon() {
        return new Coupon(CouponType.E0001);
    }

    public Member createMember() {
        return new Member();
    }
}
