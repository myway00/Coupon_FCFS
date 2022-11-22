package server.daangn.coupon.Coupon.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.dto.CouponResponseDto;
import server.daangn.coupon.Coupon.dto.TotalCouponResponseDto;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponMemberNotFoundException;
import server.daangn.coupon.Coupon.repository.CouponMemberRepository;
import server.daangn.coupon.Member.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static server.daangn.coupon.Coupon.dto.CouponRequestDtoFactory.*;

@ExtendWith(MockitoExtension.class)

public class CouponServiceTest {

    @InjectMocks CouponService couponService;
    @Mock CouponMemberRepository couponMemberRepository;
    @Mock CouponEventService couponEventService;

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
        given(couponEventService.getLeftCouponSize(CouponType.C0001)).willReturn(0L);
        given(couponEventService.getLeftCouponSize(CouponType.E0001)).willReturn(0L);

        // when
        TotalCouponResponseDto couponResponseDto = couponService.readAllCoupon();

        //then
        assertThat(couponResponseDto.getCoupons().size()).isEqualTo(coupons.size());

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
