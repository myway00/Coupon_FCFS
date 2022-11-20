package server.daangn.coupon.Coupon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.daangn.coupon.Coupon.entity.coupon.Coupon;
import server.daangn.coupon.Coupon.entity.coupon.CouponType;
import server.daangn.coupon.Coupon.exception.CouponNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired CouponRepository couponRepository ;
    @PersistenceContext EntityManager em;

    private Coupon createC0001Coupon() {
        return new Coupon(CouponType.C0001);
    }

    private Coupon createE0001Coupon() {
        return new Coupon(CouponType.E0001);
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    void createAndReadTest() {

        // given
        Coupon C0001Coupon = createC0001Coupon();
        Coupon E0001Coupon = createE0001Coupon();

        // when
        couponRepository.save(C0001Coupon);
        couponRepository.save(E0001Coupon);
        clear();

        // then
        Coupon foundC0001Coupon = couponRepository.findById(C0001Coupon.getId()).orElseThrow(CouponNotFoundException::new);
        Coupon foundE0001Coupon = couponRepository.findById(E0001Coupon.getId()).orElseThrow(CouponNotFoundException::new);

        assertThat(foundC0001Coupon.getId()).isEqualTo(C0001Coupon.getId());
        assertThat(foundE0001Coupon.getId()).isEqualTo(E0001Coupon.getId());

    }

    @Test
    void findByCouponTypeTest() {

        // given
        Coupon coupon = couponRepository.save(createC0001Coupon());
        clear();

        // when
        Coupon foundCoupon =  couponRepository.findByCouponType(CouponType.C0001).orElseThrow(CouponNotFoundException::new);

        // then
        assertThat(coupon.getId()).isEqualTo(foundCoupon.getId());

    }

}
