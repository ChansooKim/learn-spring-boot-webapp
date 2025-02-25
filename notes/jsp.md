# JSP
***
`/src/main/resources/META-INF/resources/WEB-INF/jsp/` 경로를 생성하고

```
spring.mvc.view.prefix=/WEB-INF/jsp/  
spring.mvc.view.suffix=.jsp
```
prefix(접두어)와 suffix(접미어)를 추가한 후

``` java
@RequestMapping("say-hello-jsp")  
public String sayHelloJsp() {  
    return "sayHello";  
}
```
jsp 파일명을 통해 view를 불러올 수 있다.
-> 컨트롤러와 view에만 집중할 수 있다.


** SpringBoot에서 jsp를 사용하려면 아래와 같은 의존성이 필요하다.
``` xml
<dependency>  
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>
```

/say-hello-jsp => sayHelloController - sayHelloJsp Method => sayHello.jsp

=> view resolver


### RequestParam
***
Model을 통해 Parameter를 전달할 수 있다.
``` java
@RequestMapping("login")  
public String gotoLoginPage(@RequestParam String name, ModelMap model) {  
    model.put("name", name);  
    System.out.println("Request Param is "+name);    // NOT RECOMMENDED FOR PROD CODE  
    return "login";  
}
```

jsp에서는 전달받은 파라미터를 ${ ... } 로 사용할 수 있다.
``` jsp
<html>  
    <head>
	    <title> Login Page</title>  
    </head>
    <body>
	    Welcome to the login Page
  
        ${name}!
    </body>  
</html>
```

