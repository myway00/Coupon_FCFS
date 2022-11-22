package server.daangn.coupon.Coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TotalCouponResponseDto {
    List<CouponResponseDto> coupons;
    // 전체 쿠폰 수량 (발급된 총 쿠폰)
    int totalCouponCnt;
    // 남아있는 쿠폰 수량
    long C0001LeftCnt;
    long E0001LeftCnt;
}
