package server.daangn.coupon.Common.entity.initialData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import server.daangn.coupon.Member.entity.Member;
import server.daangn.coupon.Member.repository.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")

public class InitMember {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void initDB() {
        initMember();
    }

    private void initMember() {
        for(int i =0 ; i<101 ; i ++){
            memberRepository.save(new Member());
        }
    }

}

