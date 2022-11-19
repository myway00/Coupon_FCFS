package server.daangn.coupon.Coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
