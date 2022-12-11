## 기능

- [X]  여러 종류의 선착순 쿠폰 발급 기능 구현

---

## 요청 세부사항 & 구현 방안

- [X]  1) 쿠폰 종류 2가지

|  |  의류 할인 쿠폰 | 전자제품 할인 쿠폰 |
| --- | --- | --- |
| 할인 금액 | 5만원 | 10만원 |
| 매수 | 1000장 | 300장 |
| 쿠폰코드 | C0001 | E0001 |

⊙ `CouponType.class` - Enum class로 쿠폰 종류를 관리 

- 프로젝트 실행 시 자동으로 쿠폰 정보 저장해놓도록 설정
- 쿠폰 코드를 name(식별자) 로 삼고, label 로 `매수`와 `할인 금액` 에 대한 정보를 덧붙여줌
- 쿠폰에 대한 정보가 추가되면  해당 클래스에 정보만 추가해주면 됨

- [X]  2) 사용자는 1번 쿠폰과 2번 쿠폰을 모두 발급 가능

⊙ 사용자가 쿠폰을 선착순 신청하는 요청을 보낼 때 다른 쿠폰 타입이라면 발급 가능하게 설정

- [X]  3) 사용자는 같은 쿠폰을 중복 X
⊙ 사용자가 쿠폰을 선착순 신청하는 요청을 보낼 때( member id & coupon code 정보 )  가장 먼저 CouponMember (쿠폰-멤버 다대다 테이블) 에서 해당 멤버&쿠폰으로 이뤄진 객체가 이미 있다면,`CouponDuplicateException` 예외 반환

- [X]  4)  동시에 많은 요청이 몰릴 수 있다고 가정 & 서버 애플리케이션은 단일 서버가 아닌, 멀티 서버 환경에서 동작한다고 가정 

⊙ 다중 서버 환경에서 데이터를 공유하고 동일하게 유지하기 위해 제 3의 서버 Redis를 활용 에 선착순 쿠폰 발급에 대한 정보 저장 

⊙ 테스트를 통해 동시 요청 처리가 쿠폰 초과 발급 / 에러 없이  원활하게 이뤄지는 것 확인 (로그로도 확인 가능 )

⊙ Redis 활용 방안 (아래 자체 제작 이미지 첨부)

---

![image](https://user-images.githubusercontent.com/76711238/206889554-adf747e8-7021-44a8-bac5-990b89677c08.png)
---

## Test

- [X]  CouponEventTest
- `CouponEventServiceTest`
    - 10000명의 사용자가 의류 할인 쿠폰을 선착순(1000명)으로 신청했을 시 테스트
        - 동시에 요청이 들어오더라도 REDIS 의 SCORE 정렬 순서에 따라서 대기큐에 차례로 기다리다가 때가 되면 자신의 쿠폰 발급 받기 가능
    - 10000명의 사용자가 전자제품 할인 쿠폰을 선착순(300명)으로 신청했을 시 테스트

---
