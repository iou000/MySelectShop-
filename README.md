# MySelectShop-

## 구현 화면

![로그인화면](https://user-images.githubusercontent.com/68727627/147414995-212ce6ff-ad8c-4f6a-aa83-c212a6c9ff08.JPG)
![회원가입화면](https://user-images.githubusercontent.com/68727627/147415003-a0ff5c09-70cc-422b-a29a-ffeea46392f5.JPG)
![상품검색](https://user-images.githubusercontent.com/68727627/147415001-f7bfd8fe-97a7-4742-9de8-75a41aead413.JPG)
![최저가 설정](https://user-images.githubusercontent.com/68727627/147415002-36c8c0b4-7372-4f10-8536-8b39a11ddbdc.JPG)
![메인1](https://user-images.githubusercontent.com/68727627/147414998-39ffe497-b521-4faa-8cb5-64560c5fa027.JPG)
![메인화면(폴더)](https://user-images.githubusercontent.com/68727627/147415000-0f9178db-f722-4233-8f6a-e17fb9635bc8.JPG)

로그인 > 회원가입 > 상품검색 > 최저가설정 > 등록한상품 > 상품폴더기능

### 개발환경, 사용 라이브러리
    
    spring-boot 2.5.5, lombok, springweb, spring data jpa, spring security, Thymeleaf(뷰 템플릿 엔진),json:20201115,
    spring-boot-starter-test
#### 개발툴 : IntelliJ

### main 디렉토리 구조

```bash
├─aop
├─controller 
├─dto
├─exception
├─model
├─repository
├─security   // 인증 관련
│  └─kakao
├─service
├─testdata   // 초기 실행 시 테스트데이터
└─util       // 네이버 쇼핑 검색, Validator
```

### **프로젝트 주요 기능**
    - 인증
        로그인, 소셜로그인, 회원가입
    - 상품
        회원별 등록 상품 조회
        신규 상품 등록
        설정 가격 변경
        등록된 모든 상품 목록 조회(관리자)
        상품에 폴더 추가
        네이버 쇼핑 api로 상품 조회
        
    - 폴더
        회원별 등록한 모든 폴더 조회
        회원별 폴더 추가
        회원별 등록한 폴더 내 모든 상품 조회
        
    - 부가기능(aop)
        회원별 API수행시간 조회(관리자)
        
### **ERD**
![springcoreERD](https://user-images.githubusercontent.com/68727627/147577781-17fea08a-44d2-42a8-a820-cea774f3ee3b.JPG)
