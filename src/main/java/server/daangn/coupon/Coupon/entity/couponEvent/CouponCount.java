package server.daangn.coupon.Coupon.entity.couponEvent;

import lombok.Getter;
import lombok.Setter;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;

@Getter
@Setter
public class CouponCount {

    private CouponType coupon;
    private int limit = -1;

    private static final int END = 0;

    public CouponCount(CouponType coupon) {
        this.coupon = coupon;
        this.limit = coupon.number();
    }

    public synchronized void decrease(){
        this.limit--;
    }

    public boolean end(){
        return this.limit == END ;
    }

}
