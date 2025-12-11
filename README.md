# Bookstore API

Spring Boot 기반 온라인 서점 백엔드입니다. JWT 인증/인가, 도서·장바구니·주문·리뷰·댓글·위시리스트 등을 제공합니다.

## 배포 정보
- Base URL: `http://113.198.66.68:10023`
- Swagger: `http://113.198.66.68:10023/swagger-ui/index.html`
- Health: `http://113.198.66.68:10023/health`

## 응답 규격
- 성공
```json
{
  "status": "success",
  "message": "요청이 성공했습니다.",
  "statusCode": "200",
  "data": {}
}
```
- 실패
```json
{
  "status": "error",
  "message": "에러 메시지",
  "statusCode": "400"
}
```

## RESTful 엔드포인트 요약
**Auth (/auth)**  
- POST `/auth/signup` : 회원가입(이메일 검증, 비밀번호 암호화, 중복 검사)  
- POST `/auth/login` : 로그인, JWT 발급  
- POST `/auth/refresh` : 토큰 재발급  
- POST `/auth/logout` : 로그아웃  
- PUT `/auth/profile` : (현재 `/users/me`에서 처리) 프로필/비밀번호 수정  

**Users (/users)**  
- GET `/users/me` / PATCH `/users/me` / DELETE `/users/me`  
- GET `/users` (ADMIN)  
- GET `/users/{id}` (ADMIN)  
- PATCH `/users/{id}/deactivate` (ADMIN)  

**Books (/books)**  
- POST `/books` (ADMIN)  
- GET `/books` : 페이지네이션 기본 size=20, 정렬 기본 createdAt DESC, 필터(keyword, publisher, minPrice, maxPrice, dateFrom/To)  
- GET `/books/{id}`  
- PATCH `/books/{id}` (ADMIN)  
- DELETE `/books/{id}` (ADMIN)  

**Cart (/cart)**  
- GET `/cart`  
- PUT `/cart/items` (수량 0이면 삭제)  
- DELETE `/cart/items/{cartItemId}`  
- DELETE `/cart/clear`  

**Orders (/orders)**  
- POST `/orders`  
- GET `/orders/{orderId}`  

**Wishlist (/wishlist)**  
- POST `/wishlist`  
- GET `/wishlist`  
- DELETE `/wishlist/{favoriteId}`  

**Reviews (/reviews)**  
- POST `/reviews`  
- PATCH `/reviews/{reviewId}`  
- DELETE `/reviews/{reviewId}`  
- POST `/reviews/{reviewId}/like`  
- DELETE `/reviews/{reviewId}/like`  

**Comments (/comments)**  
- POST `/reviews/{reviewId}/comments`  
- PATCH `/comments/{commentId}`  
- DELETE `/comments/{commentId}`  
- POST `/comments/{commentId}/like`  
- DELETE `/comments/{commentId}/like`  

**기타**  
- GET `/ranking` : 찜 수 기준 상위 도서  
- GET `/statistics/books/{bookId}` : 도서 통계(샘플)  
- GET `/discounts` , `/discounts/{id}` : 할인 정보(샘플)  
- GET `/settlements/sellers/{sellerId}` : 정산 정보(샘플)  

## 실행 방법
**요구사항**: JDK 25, MySQL 8.x  

**환경 변수 예시**  
```
DB_HOST=113.198.66.68
DB_PORT=3306
DB_NAME=bookstore
DB_USERNAME=<mysql-사용자>
DB_PASSWORD=<mysql-비밀번호>
JWT_SECRET=<긴 비밀키>
```

## 빌드/테스트
```
./gradlew clean build
./gradlew test
```