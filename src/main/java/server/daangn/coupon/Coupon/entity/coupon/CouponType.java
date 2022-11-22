package server.daangn.coupon.Coupon.entity.coupon;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CouponType {

    C0001("의류 할인 쿠폰", 50000, 1000),
    E0001("전자제품 할인 쿠폰", 100000, 300);

    private final String couponName;

    private final double discount;

    private final int number;

    public String couponName() {
        return couponName;
    }

    public double discount() {
        return discount;
    }

    public int number() {
        return number;
    }

}
