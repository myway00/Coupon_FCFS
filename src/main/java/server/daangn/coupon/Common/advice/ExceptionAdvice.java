package server.daangn.coupon.Common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.daangn.coupon.Common.response.Response;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponMemberNotFoundException;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Member.exception.MemberNotFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response Exception(Exception e) {
        log.info("error = {}", e.getMessage());
        return Response.failure(500, " 내부 서버 에러가 발생했어요! ");
    }

    @ExceptionHandler(CouponDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response CouponDuplicateException(CouponDuplicateException e) {
        return Response.failure(409, " 이미 발급받은 쿠폰이예요! ");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response MemberNotFoundException() {
        return Response.failure(404, " 찾을 수 없는 회원이예요! ");
    }

    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response CouponNotFoundException() {
        return Response.failure(404, " 찾을 수 없는 쿠폰이예요! ");
    }

    @ExceptionHandler(CouponMemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response CouponMemberNotFoundException() {
        return Response.failure(404, " 찾을 수 없는 쿠폰이예요! ");
    }

}
