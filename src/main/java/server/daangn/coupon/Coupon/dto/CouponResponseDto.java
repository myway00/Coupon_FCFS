package server.daangn.coupon.Coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.lang.Nullable;
import server.daangn.coupon.Coupon.entity.couponMember.CouponMember;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CouponResponseDto {

    private Long id;
    private Long couponId;
    private Long memberId;
    private String code;
    private String name;
    private double discount;
    // 사용여부 : coupon 의 updatedAt 가 null 이라면 false , null 이 아니라면 true 반환
    @Nullable
    private boolean used;
    // 쿠폰 발급 일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    // 쿠폰 사용 일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public static CouponResponseDto toDto(CouponMember couponMember){

        return new CouponResponseDto(
                couponMember.getId(),
                couponMember.getCoupon().getId(),
                couponMember.getMember().getId(),
                couponMember.getCoupon().getCouponType().name(),
                couponMember.getCoupon().getCouponType().couponName(),
                couponMember.getCoupon().getCouponType().discountPrice(),
                couponMember.getUpdatedAt() != null,
                couponMember.getCreatedAt(),
                couponMember.getUpdatedAt()
        );
    }

    public static List<CouponResponseDto> toDtoList(List<CouponMember> couponMembers){

        return couponMembers.stream().map(
                couponMember -> new CouponResponseDto(
                        couponMember.getId(),
                        couponMember.getCoupon().getId(),
                        couponMember.getMember().getId(),
                        couponMember.getCoupon().getCouponType().name(),
                        couponMember.getCoupon().getCouponType().couponName(),
                        couponMember.getCoupon().getCouponType().discountPrice(),
                        couponMember.getUpdatedAt() != null,
                        couponMember.getCreatedAt(),
                        couponMember.getUpdatedAt()
                )
        ).collect(Collectors.toList());
    }

}
