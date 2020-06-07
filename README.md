# Rest API 기반 쿠폰시스템

1. 개발 프레임워크
 - Spring Tool Suite4
 - Embedded Database(H2)

2. 문제해결 전략
 - Embedded Database(H2)를  사용하여 쿠폰 N개 생성 후 저장
 - 쿠폰 번호는 COUPON-생성일-0000001 형식으로 끝자리만 순차적으로 채번함
 - 쿠폰 생성시 userId 초기값은 '-' 로 함
 - 쿠폰 생성시 4개까지는 쿠폰만료일을 당일로 하고 이후 N개부터는 +1일씩 만료일을 늘림
 - 사용자 번호를 받아 쿠폰 지급(N번 실행시 순차적으로 채번하여 지급)
 - 사용자 ID 별 쿠폰 지급 후 사용여부 Y 로 변경
 - 쿠폰 사용 시 미등록 쿠폰/사용중 쿠폰 입력 시 오류 처리
 - 쿠폰 취소 시 미등록 쿠폰/미 사용중 쿠폰 입력 시 오류 처리
 - 사용자 ID 별 쿠폰 취소 후 사용여부 N 으로 변경
 - 당일 만료된 쿠폰 조회는 사용여부와 관계없이 만료일 기준으로 조회함
 - 발급된 쿠폰 중 3일후 만료되는 쿠폰 사용자에게 메시지 전송(사용자별 조회 기준일로부터 3일 후 만료되는 쿠폰번호 조회)

3. 빌드 및 실행 방법
 - 메이븐 빌드
 1) 쿠폰 N개 생성 
  - http://localhost:8080/ex01?cnt=? (발생 건수 입력)
  - 예) http://localhost:8080/ex01?cnt=10
 2) 사용자에게 쿠폰 지급
  - http://localhost:8080/ex02?userId=? (사용자ID 입력)
  - 예) http://localhost:8080/ex02?userId=jiko
 3) 사용자에게 지급된 쿠폰 조회
  - http://localhost:8080/ex03?userId=? (사용자ID 입력)
  - 예) http://localhost:8080/ex03?userId=jiko
 4) 사용자에게 지급된 쿠폰 사용(재사용은 불가)
  - http://localhost:8080/ex04?couponId=? (쿠폰번호 입력)
  - 예) http://localhost:8080/ex04?couponId=COUPON-20200607-0000001
 5) 사용자에게 지급된 쿠폰 사용 취소 처리(취소 후 쿠폰번호 재사용 가능)
  - http://localhost:8080/ex05?couponId=? (쿠폰번호 입력)
  - 예) http://localhost:8080/ex05?couponId=COUPON-20200607-0000001
 6) 당일 만료된 쿠폰 리스트 조회
  - http://localhost:8080/ex06?baseDt=? (만료일 입력)
  - 예) http://localhost:8080/ex06?baseDt=20200607
 7) 사용자별 3일후 만료 쿠폰 리스트 조회
  - http://localhost:8080/ex07?baseDt=?&userId=?(조회기준일과 사용자ID 입력)
  - 예) http://localhost:8080/ex07?baseDt=20200604&userId=jiko

##