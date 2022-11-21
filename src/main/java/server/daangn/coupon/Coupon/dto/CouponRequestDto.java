package server.daangn.coupon.Coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {

    @Nullable
    private Long couponId;
    @Nullable
    private String couponCode;
    @Nullable
    private Long memberId;

}
