# Bootstrap
***
> Bootstrap은 가장 널리 사용되는 CSS(Cascading Style Sheet) 프레임워크이다.

`webjars`: 수동으로 Bootstrap을 다운로드하지 않고 사용하게 해주는 의존성
``` xml
<dependency>  
    <groupId>org.webjars</groupId>  
    <artifactId>bootstrap</artifactId>  
    <version>5.1.3</version>  
</dependency>  
<dependency>  
    <groupId>org.webjars</groupId>  
    <artifactId>jquery</artifactId>  
    <version>3.6.0</version>  
</dependency>
```

- CSS 파일을 추가할 때는 `<head>` 태그 안에 넣는다.
- JavaScript 파일을 추가할 때는 항상 `<body>`의 종료 태그 바로 앞에 넣는다.


브라우저로 되돌아오는 첫 번째 응답은 `list-todos`에서 오는 것이고 ->
브라우저는 list-todos의 콘텐츠에 bootstrap css 파일에 대한 링크와 JavaScript 파일에 대한 링크가 있는 걸 발견한다 ->
브라우저는 그것들도 모두 다운로드하게 된다

``` Java Server Pages
<div class="container">
	...
</div>
```
container 클래스가 페이지 본문 전체를 담고 있는 것
=> `Bootstrap`의 권고사항 중 하나

아래와 같이 CSS를 적용하여 표를 깔끔하게 수정할 수 있다.
``` Java Server Pages
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<html>  
    <head>
		<link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet"/>  
        <title>List Todos Page</title>  
    </head>
	<body>
		<div class="container">  
            <h1>Your Todos</h1>  
            <table class="table">  
                <thead>
					<tr>
						<th>Id</th>  
                        <th>Description</th>  
                        <th>TargetDate</th>  
                        <th>Is Done?</th>  
                    </tr>
				</thead>
				<tbody>
					<c:forEach items="${todos}" var="todo">  
                        <tr>  
                            <td>${todo.id}</td>  
                            <td>${todo.description}</td>  
                            <td>${todo.targetDate}</td>  
                            <td>${todo.done}</td>  
                        </tr>
					</c:forEach>  
                </tbody>  
            </table>
		</div>
		<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
	    <script src="webjars/jquery/3.6.0/jquery.min.js"></script>
    </body>
</html>
```