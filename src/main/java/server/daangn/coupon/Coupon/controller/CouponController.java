package server.daangn.coupon.Coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.daangn.coupon.Common.response.Response;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.service.CouponService;

import javax.validation.Valid;

import static server.daangn.coupon.Common.response.Response.success;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupon")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCoupon(@Valid @RequestBody CouponRequestDto req) {
        return success(couponService.createCoupon(req));
    }

    @PutMapping("/coupon")
    @ResponseStatus(HttpStatus.OK)
    public Response useCoupon(@Valid @RequestBody CouponRequestDto req) {
        return success(couponService.useCoupon(req));
    }

    @GetMapping("/coupon/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response readCoupon(@PathVariable Long id) {
        return success(couponService.readCoupon(id));
    }

    @GetMapping("/coupons")
    @ResponseStatus(HttpStatus.OK)
    public Response readAllCoupon() {
        return success(couponService.readAllCoupon());
    }

}
