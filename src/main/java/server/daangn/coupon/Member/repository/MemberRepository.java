package server.daangn.coupon.Member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import server.daangn.coupon.Member.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
