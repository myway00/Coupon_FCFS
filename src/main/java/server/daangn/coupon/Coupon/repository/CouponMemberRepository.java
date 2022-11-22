package server.daangn.coupon.Coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Member.entity.Member;

import java.util.Optional;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {

    Optional<CouponMember> findByMember(Member member);
    Optional<CouponMember> findByCoupon(Coupon coupon);
    Optional<CouponMember> findByMemberAndCoupon( Member member,Coupon coupon);
}
