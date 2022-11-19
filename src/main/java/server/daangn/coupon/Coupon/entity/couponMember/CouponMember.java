package server.daangn.coupon.Coupon.entity.couponMember;

import lombok.*;
import server.daangn.coupon.Common.entity.EntityDate;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CouponMemberId.class)
public class CouponMember extends EntityDate implements Serializable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column
    private LocalDate sopStartPeriod;

}
