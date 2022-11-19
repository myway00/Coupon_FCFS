package server.daangn.coupon.Member.entity;

import lombok.*;
import server.daangn.coupon.Common.entity.EntityDate;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
