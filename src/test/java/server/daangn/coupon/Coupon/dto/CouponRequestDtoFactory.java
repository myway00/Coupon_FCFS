package server.daangn.coupon.Coupon.dto;

public class CouponRequestDtoFactory {

    // null
    public static CouponRequestDto createNullCouponRequest(){
        return new CouponRequestDto( null, null, null);
    }

    // 쿠폰 발급 요청 - member id, coupon code
    public static CouponRequestDto createCouponRequest(){
        return new CouponRequestDto( null, "E0001", 1L);
    }

    // 쿠폰 사용 요청 - member id, coupon id
    public static CouponRequestDto useCouponRequest(){
        return new CouponRequestDto( 1L, null, 1L);
    }

    // 사용자의 쿠폰 조회 - member id
    public static final CouponRequestDto readCouponRequest(){
        return new CouponRequestDto( null, null, 1L);
    }

}
