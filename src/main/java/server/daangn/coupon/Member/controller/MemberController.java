package server.daangn.coupon.Member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import server.daangn.coupon.Common.response.Response;
import server.daangn.coupon.Member.service.MemberService;

import static server.daangn.coupon.Common.response.Response.success;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 멤버 쿠폰 조회 (멤버 아이디)
    @GetMapping("/member-coupon/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response readMemberCoupon(@PathVariable Long id) {
        return success(memberService.readMemberCoupon(id));
    }

}
