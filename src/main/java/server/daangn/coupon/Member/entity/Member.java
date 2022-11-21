package server.daangn.coupon.Member.entity;

import lombok.*;
import server.daangn.coupon.Common.entity.EntityDate;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Member extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CouponMember> couponMember = new HashSet<>();

    public void addCoupon(CouponMember couponMember){
        this.couponMember.add(couponMember);
    }

}
