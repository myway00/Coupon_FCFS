package server.daangn.coupon.Coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;
import server.daangn.coupon.Member.entity.Member;

import java.util.Optional;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {
    Optional<Member> findByMember(Member member);
}
