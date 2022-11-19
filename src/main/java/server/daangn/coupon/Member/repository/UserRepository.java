package server.daangn.coupon.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Member.entity.Member;

public interface UserRepository extends JpaRepository<Member, Long> {

}
