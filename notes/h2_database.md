# H2 Database와 Spring Data JPA
***
의존성 추가
``` xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-data-jpa</artifactId>  
</dependency>  
<dependency>  
    <groupId>com.h2database</groupId>  
    <artifactId>h2</artifactId>  
    <scope>runtime</scope>  
</dependency>
```

`<scope>`를 runtime으로 지정하면, H2 데이터베이스가 jar 파일의 일부로 제공되지 않게 한다.

Spring Security 환경에서 H2를 추가하면 아래와 같이 h2의 주소도 계속 변경된다.
`H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:83bd1161-6efb-409f-bdfc-b25b67b1871a'`

application.properties에 datasource url을 지정해준다.
`spring.datasource.url=jdbc:h2:mem:testdb`

### JPA
***
SpringBoot는 H2 Database를 발견하면 즉시 그걸 사전 설정하기 시작한다.
사전 설정 과정에서 Entity가 발견되면, 그 테이블을 즉시 생성한다.

->
SpringBoot 자동 설정이 클래스 경로에서 h2 또는 인메모리 데이터베이스를 발견해서 모든 엔티티에 대해 곧바로 테이블을 생성한다.

Entity의 기본값은 클래스 이름 자체이다.
`@Entity(name = "TodoABC)` 다른 테이블명을 설정하면 이 또한 h2에 바로 매핑된다.

열(Column) 이름도 `@Column(name="name")`을 통해 자유롭게 지정할 수 있다.

** 데이터베이스에서는 일반적으로 밑줄을 써서 단어들을 구분한다.
필드명에 대문자가 있다면 언더스코어('\_')로 변환된다.
`targetDate -> TARGET_DATE`

resources 경로의 `data.sql`로 쿼리를 만들 수 있고, 이 쿼리로 h2의 기본 데이터를 생성할 수 있다.
--> `data.sql`을 설정하면 오류가 나타나는데
기본적으로 `data.sql`은 엔티티가 처리되기 전에 실행되기 때문이다.

** 테이블은 엔티티가 처리될 때 생성되고, `data.sql`은 테이블이 생성되기 전에 실행된다.

application.properties에 아래 구문을 추가하면
```
spring.jpa.defer-datasource-initialization=true
```

`data.sql`이 정상 작동된다.

application.properties에 아래 라인을 추가하여, 생성되고 있는 JPA 쿼리를 확인한다.
`spring.jpa.show-sql=true`

##### 요약
***
Spring-Data-JPA와 H2 의존성을 추가하면
Spring Boot 자동 설정(Auto Configuration)이 수행하는 기능
- JPA 및 Spring Data JPA 프레임워크 초기화
- 인메모리 데이터베이스(H2) 실행
- 애플리케이션과 인메모리 데이터베이스 간의 연결 설정
- 애플리케이션 시작 시 몇 개의 스크립트 실행 (예: `data.sql`, `schema.sql`)

H2는 인메모리 데이터베이스이다
- 데이터를 영구 저장하지 않음
- 학습용으로 훌륭함
- 하지만 프로덕션 환경에는 적합하지 않음
