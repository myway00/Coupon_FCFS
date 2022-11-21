package server.daangn.coupon.Coupon.entity.couponMember;

import lombok.*;
import server.daangn.coupon.Common.entity.EntityDate;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
public class CouponMember extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon")
    private Coupon coupon;

    public void  useCoupon(){
        update(); // coupon의 updatedAt을 현재 시간으로 갱신
    }

    public CouponMember (Member member, Coupon coupon) {
        this.member = member;
        this.coupon = coupon;
    }

}
