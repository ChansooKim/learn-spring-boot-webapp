# Logging
***
SpringBoot에서는 application.properties를 통해 로깅을 설정할 수 있다.

보통 개발 시 debug 레벨을 사용하지만, 프로덕션 수준에서는 info를 사용하기도 한다.
info 레벨을 사용하면 프린트되는 로그의 양이 훨씬 줄어든다.

``` properties
logging.level.org.springframework=info  
logging.level.com.csk.springboot.myfirstwebapp=debug
```
패키지 경로를 `logging.level`에 등록해 해당 패키지만 지정된 레벨의 로깅을 사용하도록 할 수 있다.

로그를 프린트하기 위해 `System.out.println`을 사용하는 것은 권장하지 않는다.
-> 가장 좋은 방법은 Logger를 이용하는 것
``` java
private Logger logger = LoggerFactory.getLogger(getClass());

logger.debug("Request Param is {}", name);
```

SpringBoot는 로깅 활성화를 위해 `spring-boot-starter-logging`을 제공한다.
- `spring-boot-starter-web`의존성을 추가하면, 로깅은 자동으로 포함되어 있다.
- SpringBoot가 사용하는 기본 로깅 프레임워크는 `SLF4j`를 사용하는 `Logback`이다.