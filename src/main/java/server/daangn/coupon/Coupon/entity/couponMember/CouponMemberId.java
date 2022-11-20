package server.daangn.coupon.Coupon.entity.couponMember;

import lombok.*;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Member.entity.Member;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponMemberId implements Serializable {

    private Member member;
    private Coupon coupon;

}
