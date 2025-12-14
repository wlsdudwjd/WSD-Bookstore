# Bookstore API

Spring Boot 기반 온라인 서점 백엔드입니다. JWT 인증/인가, 도서·장바구니·주문·리뷰·댓글·위시리스트 등을 제공합니다.

## 배포 정보
- Base URL: [http://113.198.66.68:10118](http://113.198.66.68:10118)
- Swagger: [http://113.198.66.68:10118/swagger-ui/index.html](http://113.198.66.68:10118/swagger-ui/index.html)
- Health: [http://113.198.66.68:10118/health](http://113.198.66.68:10118/health)

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

### Swagger UI 캡처:
  - ![Swagger UI 1](static/swagger.png)
  - ![Swagger UI 2](static/swagger2.png)

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

**로컬 실행 예시**
```
./gradlew clean build -x test
java -jar build/libs/bookstore-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url="jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?serverTimezone=Asia/Seoul&characterEncoding=UTF-8" \
  --spring.datasource.username=$DB_USERNAME \
  --spring.datasource.password=$DB_PASSWORD \
  --server.port=10023
```

## JCloud 서버 준비
Ubuntu 기준 설치 커맨드입니다. 이미 설치되어 있다면 건너뛰세요.
- Java 21+ (OpenJDK):  
  ```bash
  sudo apt update
  sudo apt install -y openjdk-21-jdk
  java -version
  ```
- MySQL 8.x:  
  ```bash
  sudo apt install -y mysql-server
  sudo systemctl enable --now mysql
  sudo mysql_secure_installation
  # DB/계정 생성 예시
  mysql -u root -p -e "CREATE DATABASE bookstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
  mysql -u root -p -e "CREATE USER 'jjy'@'%' IDENTIFIED BY '123'; GRANT ALL PRIVILEGES ON bookstore.* TO 'jjy'@'%'; FLUSH PRIVILEGES;"
  ```
- Node/npm & PM2 (무중단 구동):  
  ```bash
  sudo apt install -y nodejs npm
  sudo npm install -g pm2
  pm2 --version
  ```
- PM2 실행 예시(서버에서):  
  ```bash
  pm2 start "java -jar /home/ubuntu/bookstore-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod" --name bookstore \
    --env DB_HOST=localhost --env DB_PORT=3306 --env DB_NAME=bookstore \
    --env DB_USERNAME=jjy --env DB_PASSWORD=123 \
    --env JWT_SECRET="your-32bytes-or-longer-secret"
  pm2 save
  pm2 startup    # 재부팅 후 자동 시작 등록
  ```

## PM2 ecosystem.config.js 예시
```js
module.exports = {
  apps: [
    {
      name: "bookstore",
      cwd: "/home/ubuntu",
      script: "java",
      args: "-jar bookstore-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod",
      instances: 1,
      autorestart: true,
      watch: false,
      max_memory_restart: "512M",
      env: {
        DB_HOST: "localhost",
        DB_PORT: "3306",
        DB_NAME: "example",
        DB_USERNAME: "example",
        DB_PASSWORD: "example",
        JWT_SECRET: "example"
      }
    }
  ]
}
자세한 값은 .env에서 확인 가능
```

## 추가 자료
- API 설계서(PDF): `static/API-docs.pdf`

```
