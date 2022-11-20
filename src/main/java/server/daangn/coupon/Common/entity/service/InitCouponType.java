package server.daangn.coupon.Common.entity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.repository.CouponRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")

/**
 * 쿠폰 타입을 h2 DB 에 미리 생성하는 클래스
 */
public class InitCouponType {

    private final CouponRepository couponRepository;

    @PostConstruct
    public void initDB() {
        initCouponType();
    }

    private void initCouponType() {
        couponRepository.saveAll(
                List.of(CouponType.values()).stream().map(
                        couponType ->
                        new Coupon(
                                couponType
                        ))
                        .collect(Collectors.toList())
        );
    }
}
