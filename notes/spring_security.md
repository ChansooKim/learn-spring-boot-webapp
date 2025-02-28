# Spring Security
***
의존성 추가
``` xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-security</artifactId>  
</dependency>
```

의존성을 추가하고, 서버를 실행하자마자 아래와 같은 로그가 나타난다.
```
Using generated security password: a73348d1-7f43-4b84-84b6-7ebc7dbf27a6

This generated password is for development use only. Your security configuration must be updated before running your application in production.
```
서버에 접속하면, 로그인을 해야만 웹 사이트를 이용할 수 있도록 변경된다.
=> 어떤 url에 접속하든 로그인 없이는 사용하지 못하도록 로그인 페이지로 리다이렉트된다.

로그에 있는 `security password`와 `user`를 이용해 로그인할 수 있다.

##### Default User 설정
``` java
@Configuration  
public class SpringSecurityConfiguration {  
    @Bean  
    public InMemoryUserDetailsManager createUserDetailsManager() {  
        UserDetails userDetails = User.withDefaultPasswordEncoder()  
                .username("in28minutes")  
                .password("dummy")  
                .roles("ADMIN")  
                .build();
        return new InMemoryUserDetailsManager(userDetails);  
    }  
}
```
InMemoryUserDatailsManager를 통해 기본 username, password를 설정할 수 있다.

-> 하지만, `withDefaultPasswordEncoder()`는 Deprecated 함수이기 때문에 변경한다.
BCrypt 스트롱 해싱 함수를 사용하는 `BCryptPasswordEncoder()`로 변경한다.

``` java
@Bean  
public PasswordEncoder passwordEncoder() {  
    return new BCryptPasswordEncoder();  
}
```
위 Bean을 사용하면, 기존에 DefaultPassword로 설정한 "dummy"로 로그인이 안되는데
모든 입력값에 대해 Spring Security가 위 알고리즘을 통해 인코딩하기 때문에 로그인이 되지 않는 것

``` java
@Bean  
public InMemoryUserDetailsManager createUserDetailsManager() {  
    Function<String, String> passwordEncoder  
            = input -> passwordEncoder().encode(input);  
  
    UserDetails userDetails = User.builder()  
            .passwordEncoder(passwordEncoder)  
            .username("in28minutes")  
            .password("dummy")  
            .roles("ADMIN")  
            .build();  
  
    return new InMemoryUserDetailsManager(userDetails);  
}
```
passwordEncoder를 별도로 설정해준다.

``` java
@Configuration  
public class SpringSecurityConfiguration {  
    public InMemoryUserDetailsManager createUserDetailsManager() {  
        UserDetails userDetails1 = createNewUser("in28minutes", "dummy");  
        UserDetails userDetails2 = createNewUser("Ranga", "dummydummy");  
        
        return new InMemoryUserDetailsManager(userDetails1, userDetails2);  
    }  
  
    private UserDetails createNewUser(String username, String password) {  
        Function<String, String> passwordEncoder  
                = input -> passwordEncoder().encode(input);  
                 
        UserDetails userDetails = User.builder()  
                .passwordEncoder(passwordEncoder)  
                .username(username)  
                .password(password)  
                .roles("ADMIN")  
                .build();  
        return userDetails;  
    }  
  
    @Bean  
    public PasswordEncoder passwordEncoder() {  
        return new BCryptPasswordEncoder();  
    }  
}
```
여러개의 defaultUser를 추가할 수 있다.

##### H2 Database
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


Spring Security는 기본값으로 아무것도 설정하지 않으면 두 가지가 설정된다.
1. 모든 URL이 보호된다.
2. 승인되지 않은 요청에 대해서는 로그인 양식이 표시된다.
    1) H2 콘솔에 엑세스하려면, CSRF(사이트 간 요청 위조)를 비활성화해야한다.
    2) Spring Security는 기본값으로 프레임을 허용하지 않으니, 프레임을 허용해줘야 한다.

##### SecurityFilterChain을 설정해준다.
``` java
@Bean  
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  
    http.authorizeHttpRequests(  
    auth -> auth.anyRequest().authenticated());  
    http.formLogin(withDefaults());  
    http.csrf().disable();  
    http.headers().frameOptions().disable();  
    return http.build();  
}
```
-> Spring Security 6.1버전 이후부터는 람다 스타일의 API가 권장된다.

``` java
@Bean  
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  
  
    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())  
            .formLogin(withDefaults())  
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(  
                    frameOptions -> frameOptions.disable()
            ).disable());
	return http.build();
}
```


### 요약
***
Spring Security는 기본값으로 아무것도 설정하지 않으면 두 가지가 설정된다.
1. 모든 URL이 보호된다.
2. 승인되지 않은 요청에 대해서는 로그인 양식이 표시된다.
    1) H2 콘솔에 엑세스하려면, CSRF(사이트 간 요청 위조)를 비활성화해야한다.
    2) Spring Security는 기본값으로 프레임을 허용하지 않으니, 프레임을 허용해줘야 한다.