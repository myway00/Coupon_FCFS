package server.daangn.coupon.Coupon.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponMemberNotFoundException;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Coupon.repository.CouponMemberRepository;
import server.daangn.coupon.Coupon.repository.CouponRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static server.daangn.coupon.Coupon.dto.CouponRequestDtoFactory.*;

@ExtendWith(MockitoExtension.class)

public class CouponServiceTest {

    @InjectMocks CouponService couponService;
    @Mock CouponMemberRepository couponMemberRepository;
    @Mock MemberRepository memberRepository;
    @Mock CouponRepository couponRepository;

    @Test
    void validateCouponExceptionByDuplicateCouponTest(){

        // given
        CouponMember couponMember = createCouponMember();
        given(couponMemberRepository.findByMemberAndCoupon(any(), any())).willReturn(Optional.of(couponMember));

        // when, then
        assertThatThrownBy(() -> couponService.checkDuplicateCoupon(any(), any()))
                .isInstanceOf(CouponDuplicateException.class);

    }

    @Test
    void validateCouponExceptionByCouponNotFoundTest(){

        // given
        given(memberRepository.findById(any())).willReturn(Optional.of(createMember()));
        given(couponRepository.findByCouponType(any())).willReturn(Optional.of(createCoupon()));
        given(couponMemberRepository.findByMemberAndCoupon(any(), any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> couponService.createCoupon(createCouponRequest()))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    void useCouponTest(){

        // given
        given(couponMemberRepository.findById(anyLong())).willReturn(Optional.of(createCouponMember()));
        CouponRequestDto req = useCouponRequest();

        //when
        couponService.useCoupon(req);

        // then
        assertThat(couponMemberRepository.findById(1L).orElseThrow(CouponMemberNotFoundException::new).getUpdatedAt()).isNotNull();

    }

    @Test
    void useCouponExceptionByCouponNotFoundTest() {

        // given
        given(couponMemberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> couponService.useCoupon(useCouponRequest()))
                .isInstanceOf(CouponMemberNotFoundException.class);

    }

    @Test
    void readCouponTest(){

        // given
        CouponMember coupon = createCouponMember();
        given(couponMemberRepository.findById(anyLong())).willReturn(Optional.of(createCouponMember()));

        // when
        CouponResponseDto couponResponseDto = couponService.readCoupon(1L);

        //then
        assertThat(couponResponseDto.getId()).isEqualTo(coupon.getId());

    }

    @Test
    void readCouponExceptionByCouponNotFoundTest() {

        // given
        given(couponMemberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> couponService.readCoupon(anyLong()))
                .isInstanceOf(CouponMemberNotFoundException.class);

    }

    @Test
    void readAllCouponTest(){

        // given
        List<CouponMember> coupons = createCouponMembers();
        given(couponMemberRepository.findAll()).willReturn(coupons);

        // when
        List<CouponResponseDto> couponResponseDto = couponService.readAllCoupon();

        //then
        assertThat(couponResponseDto.size()).isEqualTo(coupons.size());

    }

    public Coupon createCoupon() {
        return new Coupon(CouponType.E0001);
    }

    public Member createMember() {
        return new Member();
    }

    public CouponMember createCouponMember(){
        return new CouponMember(createMember(), createCoupon());
    }

    public List<CouponMember> createCouponMembers(){
        List<CouponMember> couponMembers = new ArrayList<>();
        for(int i=0; i<5 ; i++){
            couponMembers.add(new CouponMember(createMember(), createCoupon()));
        }
        return couponMembers;
    }

}
