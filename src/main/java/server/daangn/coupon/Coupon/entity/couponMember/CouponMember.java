package server.daangn.coupon.Coupon.entity.couponMember;

import lombok.*;
import server.daangn.coupon.Common.entity.EntityDate;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@IdClass(CouponMemberId.class)
public class CouponMember extends EntityDate implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon")
    private Coupon coupon;

}
