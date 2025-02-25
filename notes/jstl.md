# JSTL
***
> 동적 콘텐츠를 중심으로 더 복잡한 것을 하고 싶은 경우 JSTL 태그를 사용한다.

Maven 의존성
``` xml
<dependency>  
    <groupId>jakarta.servlet.jsp.jstl</groupId>  
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.eclipse.jetty</groupId>  
    <artifactId>glassfish-jstl</artifactId>  
    <version>11.0.20</version>  
</dependency>
```

JSP에는
`<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>` 태그를 추가한다.

JSTL core tags
https://docs.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/c/tld-summary.html
-> 사용하고자 하는 JSTL 태그를 가져온다.

``` jsp
<c:forEach items="${todos}" var="todo"> </c:forEach>
```
여러 태그를 사용할 수 있다.