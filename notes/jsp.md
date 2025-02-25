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


### Login Page
***
``` jsp
<html>  
    <head>
		<title> Login Page</title>  
    </head>
	<body>
		Welcome to the login Page!  
        <form>  
            Name: <input type="text" name="name">  
            Password: <input type="password" name="password">  
            <input type="submit">  
        </form>
	</body>
</html>
```
form에서 input을 통해 입력 후 submit으로 제출 시
`http://localhost:8080/login?name=name&password=password`
url에 파라미터로 자동으로 추가된다.
-> 정보를 url에서 전송하는 건 안전하지 않다.
직접 애플리케이션에 액세스하는 것이 아니라 인터넷을 통해 액세스하기 때문
인터넷에 존재하는 수많은 라우터들은 이 url을 볼 수 있다.

GET 요청을 사용하면, 모든 정보가 url의 일부로서 전송된다.
-> 안전하지 않음

따라서, form을 사용할 때 POST 요청을 사용한다.
``` jsp
<form method="post">  
    Name: <input type="text" name="name">  
    Password: <input type="password" name="password">  
    <input type="submit">  
</form>
```

웹 사이트에 전송하려는 모든 보안 정보는 항상 POST를 사용하는 것이 좋다.


#### WelcomePage로 리다이렉션
***
``` java
@Controller  
public class LoginController {  
    @RequestMapping(value="login", method= RequestMethod.GET)  
    public String gotoLoginPage() {  
        return "login";  
    }
    
    @RequestMapping(value="login", method= RequestMethod.POST)  
    public String gotoWelcomePage(@RequestParam String name,  
                                  @RequestParam String password, ModelMap model) {  
        model.put("name", name);  
        model.put("password", password);  
        return "welcome";  
    }  
}
```
동일한 엔드포인트로 HTTP Method를 다르게 사용하여
login과 welcome page를 구성한다.

url에 `/login`을 통해 접속한 로그인 페이지에서
name과 password를 입력 후 submit하면, `welcome.jsp`페이지로 리다이렉션한다.


#### Session vs Model vs Request
***
Controller에서 Model에 값을 넣고 jsp에서 활용할 수 있는데
전달받은 Model 값은 다른 요청을 통해 페이지가 이동하면 사라진다.
``` java
@RequestMapping(value="login", method= RequestMethod.POST)  
public String gotoWelcomePage(@RequestParam String name,  
                              @RequestParam String password, ModelMap model) {  
    if(authenticationService.authenticate(name, password)) {  
        model.put("name", name);  
        model.put("password", password);  
        return "welcome";  
    }  
    model.put("errorMessage", "Invalid Credentials! Please try again.");  
    return "login";  
}
```
위 구간에서는 model에 있는 `name`을 사용할 수 있지만
``` jsp
<body>  
    <div>Welcome to in28minutes</div>  
    <div>Your Name: ${name}</div>  
    <div><a href="list-todos">Manage</a> your todos</div>  
</body>
```

`list-todos`를 통해 접근한 `listTodos`에서는 `name`이 소멸되어 사용할 수 없다.
``` jsp
<html>  
<head>  
    <title>List Todos Page</title>  
</head>  
<body>  
    <div>Welcome ${name}</div>  
    <div>Your Todos are ${todos}</div>  
</body>  
</html>
```
=> 여러 요청에 걸쳐 값을 유지하길 원한다면, 세션을 사용해야 한다.

컨트롤러 상단에
`@SessionAttributes("name")`를 추가하면,
model에 넣은 값 `name`이 세션에 추가되고

다른 요청에서도 `name`을 사용할 수 있다.
``` java
@Controller  
@SessionAttributes("name")
public class LoginController {
	@RequestMapping(value="login", method= RequestMethod.POST)  
	public String gotoWelcomePage(@RequestParam String name,  
							   @RequestParam String password, ModelMap model) {  
	    if(authenticationService.authenticate(name, password)) {  
	        model.put("name", name);  
	        model.put("password", password);
	        return "welcome";
		}
	}
}
```

##### Session vs Request Scopes
브라우저에서 오는 모든 요청은 서버에 배포된 웹 애플리케이션에 의해 처리된다. </br>


RequestScope: 오직 하나의 요청에만 적용된다.
- 요청이 다시 전송되면 요청 속성은 메모리에서 삭제된다.
- 그 이후에 이루어지는 요청에서 사용할 수 없고, 대부분의 사례에는 이렇게 하는 게 좋다. </br>

SessionScope: 세부 정보가 다수의 요청에 걸쳐 저장된다.
- 세션에 저장할 때는 각별한 주의가 필요하다.
  -> 추가로 메모리를 차지하고, 모든 세부 정보가 서버에 저장되기 때문
	

