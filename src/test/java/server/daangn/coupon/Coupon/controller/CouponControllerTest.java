package server.daangn.coupon.Coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.daangn.coupon.Coupon.dto.CouponRequestDto;
import server.daangn.coupon.Coupon.service.CouponService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static server.daangn.coupon.Coupon.dto.CouponRequestDtoFactory.*;

@ExtendWith(MockitoExtension.class)
public class CouponControllerTest {

    @InjectMocks CouponController couponController;
    @Mock CouponService couponService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    @Test
    void createCouponAPITest() throws Exception {
        // given
        CouponRequestDto req = createNullCouponRequest();

        // when, then
        mockMvc.perform(
                        post("/coupon")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(couponService).createCoupon(refEq(req));
    }

    @Test
    void useCouponAPITest() throws Exception {
        // given
        CouponRequestDto req = createNullCouponRequest();

        // when, then
        mockMvc.perform(
                        put("/coupon")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(couponService).useCoupon(req);
    }

    @Test
    void readCouponAPITest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                        get("/coupon/{id}", id))
                .andExpect(status().isOk());

        verify(couponService).readCoupon(id);
    }

    @Test
    void readAllCouponAPITest() throws Exception {

        // when, then
        mockMvc.perform(
                        get("/coupons"))
                .andExpect(status().isOk());

        verify(couponService).readAllCoupon();
    }

}
