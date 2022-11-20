package server.daangn.coupon.Coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponType(CouponType couponType);
}
