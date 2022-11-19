package server.daangn.coupon.Coupon.entity.coupon;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CouponType couponType;

    public Coupon(CouponType couponType) {
        this.couponType = couponType;
    }

}
