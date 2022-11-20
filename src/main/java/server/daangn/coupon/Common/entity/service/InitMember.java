package server.daangn.coupon.Common.entity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.repository.CouponRepository;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")

public class InitMember {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void initDB() {
        initMember();
    }

    private void initMember() {

        for(int i =0 ; i<50 ; i ++){
            memberRepository.save(new Member());
        }

    }
}

