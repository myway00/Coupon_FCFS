package server.daangn.coupon.Coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final CouponEventService couponEventService;
    private static boolean C00001InitFlag;
    private static boolean E00001InitFlag;
    private static boolean C00001EndFlag;
    private static boolean E00001EndFlag;

    /**
     * < C0001 쿠폰 발급 스케쥴러 >
     * 0.5초에 한번씩 쿠폰 발급 (publish) & 대기번호 갱신 (get order) 진행
     */
    @Scheduled(fixedDelay = 500)
    private void C0001CouponScheduler() {
        if (!C00001EndFlag) {
            if (!C00001InitFlag) {
                C00001InitFlag = true;
                couponEventService.setCouponCount(CouponType.C0001);
                couponEventService.initRedisQueue(CouponType.C0001);
                couponEventService.initPublishList(CouponType.C0001);
            }

            if (couponEventService.validEnd(CouponType.C0001)) {
                log.info("=====  " + CouponType.C0001.couponName() + "선착순 쿠폰 발급 종료 =====");
                C00001EndFlag = true;
            }
            couponEventService.publish(CouponType.C0001);
            couponEventService.getOrder(CouponType.C0001);
        }
    }

    /**
     * < E0001 쿠폰 발급 스케쥴러 >
     * 0.5초에 한번씩 쿠폰 발급 (publish) & 대기번호 갱신 (get order) 진행
     */
    @Scheduled(fixedDelay = 500)
    private void E0001CouponScheduler() {
        if (!E00001EndFlag) {
            if (!E00001InitFlag) {
                E00001InitFlag = true;
                couponEventService.setCouponCount(CouponType.E0001);
                couponEventService.initRedisQueue(CouponType.E0001);
                couponEventService.initPublishList(CouponType.E0001);
            }

            if (couponEventService.validEnd(CouponType.E0001)) {
                log.info("===== " + CouponType.E0001.couponName() + " 선착순 쿠폰 발급 종료 =====");
                E00001EndFlag = true;
            }
            couponEventService.publish(CouponType.E0001);
            couponEventService.getOrder(CouponType.E0001);
        }
    }

    /**
     * coupon type에 해당하는 선착순 이벤트가 진행 / 종료 여부를 알 수 있게 하는 함수
     * @param couponType
     * @return 진행 중 : true / 종료 : false
     */
    public static boolean isInProgress(CouponType couponType){
        switch(couponType.name()){
            case ("C0001"):
                return !C00001EndFlag;
            case ("E0001"):
                return !E00001EndFlag;
            default:
                throw new CouponNotFoundException();
        }
    }
}
