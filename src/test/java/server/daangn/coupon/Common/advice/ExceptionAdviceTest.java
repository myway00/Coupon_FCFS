package server.daangn.coupon.Common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.daangn.coupon.Coupon.controller.CouponController;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.exception.CouponDuplicateException;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;
import server.daangn.coupon.Coupon.service.CouponService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.daangn.coupon.Coupon.dto.CouponRequestDtoFactory.createCouponRequest;
import static server.daangn.coupon.Coupon.dto.CouponRequestDtoFactory.readCouponRequest;

@ExtendWith(MockitoExtension.class)
public class ExceptionAdviceTest {
    @InjectMocks CouponController couponController;
    @Mock CouponService couponService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(couponController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void duplicateCouponExceptionTest() throws Exception {
        // given
        CouponRequestDto req = createCouponRequest();
        given(couponService.createCoupon(any())).willThrow(CouponDuplicateException.class);

        // when, then
        mockMvc.perform(
                        post("/coupon")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void notFoundCouponExceptionTest() throws Exception {
        // given
        CouponRequestDto req = readCouponRequest();
        given(couponService.readCoupon(anyLong())).willThrow(CouponNotFoundException.class);

        // when, then
        mockMvc.perform(
                        get("/coupon/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

}
