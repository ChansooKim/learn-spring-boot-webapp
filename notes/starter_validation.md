# SpringBoot Starter Validation
***
``` java server pages
<form method="post">  
    Description: <input type="text" name="description"  
                    required="required"/>  
    <input type="submit" class="btn btn-success"/>  
</form>
```

> JavaScript에서 `required`를 통해 form에 대한 검증을 할 수 있다.
> 하지만, HTML에서 JavaScript를 통해 구현한 검증은 해커가 아주 쉽게 건너뛸 수 있다.
>
=> 따라서, 항상 최선의 방식은 서버 측 검증(Server-Side Validation)이다.


### Validations with SpringBoot
***
1. 검증과 관련된 starter 프로젝트를 임포트한다.
    - pom.xml
2. Command Bean(Form Backing Object)를 사용한다.
    - 2-way binding`[양방향바인딩]`(`todo.jsp` & `TodoController.java`)
3. Bean에 검증을 추가한다.
    - `Todo.java`
        - 검증을 추가하고, 설명 필드에 대해 최소 문자 개수가 10개라고 알릴 수 있다.
        - 목표 날짜에 대해서는 항상 현재 날짜 또는 미래 날짜가 되길 원한다고 할 수 있다.
4. 검증 오류를 뷰에 표시한다.
    - `todo.jsp`

spring-boot-starter-validation 의존성
``` xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-validation</artifactId>  
</dependency>
```

form tag library
https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/view.html

1. JSP에 form tag를 추가하고
``` java server pages
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
```
2. `<form:from ... >` 태그로 변경한다.
``` java server pages
<form:form method="post" modelAttribute="todo">  
    Description: <form:input type="text" path="description"  
                    required="required"/>  
    <form:input type="hidden" path="id"/>  
    <form:input type="hidden" path="done"/>  
    <input type="submit" class="btn btn-success"/>  
</form:form>
```
받아주는 필드 id, done을 만들고

3. Get 메서드에서 빈 값을 바인딩 해준다.
   `Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false); `
   -> todo를 입력하는 페이지인 `todo.jsp`를 불러올 때, 빈 값이 바인딩되어 있다.
``` java
@RequestMapping(value = "add-todo", method = RequestMethod.GET)  
public String showNewTodoPage(ModelMap model) {  
    String username = (String) model.get("name");  
    Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);  
    model.put("todo", todo);  
    return "todo";  
}
```

`단방향 바인딩`
``` java
Todo todo = new Todo(0, username, "Default Desc",  
        LocalDate.now().plusYears(1), false);
```
Controller에서 양식에 표시되는 값으로 바인딩

새로운 값을 form에 입력하면, 이 값은 Post 요청을 통해 매핑되어야 한다.
submit 버튼을 클릭하면, Post 요청을 하게 되고
-> 이것은 Todo에 이루어지는 바인딩이다.

Todo의 description 필드에 새로운 값이 바인딩된다.
==> 이것을 `양방향 바인딩`이라고 부른다.

**Bean에서 form으로 바인딩 되고, form에서 Bean으로 바인딩된다.**

** 양방향 바인딩은 많은 유연성을 제공해준다.

4. Bean에 검증을 추가한다.
``` java
public class Todo {  
	...
    @Size(min = 10, message = "Enter at least 10 characters")  
    private String description;  
}

@RequestMapping(value = "add-todo", method = RequestMethod.POST)  
public String addNewTodo(ModelMap model, @Valid Todo todo) {  
    String username = (String) model.get("name");  
    todoService.addTodo(username, todo.getDescription(),  
            LocalDate.now().plusYears(1), false);  
    return "redirect:list-todos";  
}
```
`@Size`와 같이 필드에 검증을 추가하고, Controller에서 Bean을 바인딩하는 곳에  `@Valid` 어노테이션을 추가한다.

이렇게 하면, 바인딩이 이루어지기 전에 Bean을 검증하게 된다.

5. BindingResult 추가
``` java
@RequestMapping(value = "add-todo", method = RequestMethod.POST)  
public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {  
    if(result.hasErrors()) {  
        return "todo";  
    }  
    String username = (String) model.get("name");  
    todoService.addTodo(username, todo.getDescription(),  
            LocalDate.now().plusYears(1), false);  
    return "redirect:list-todos";  
}
```
BindingReulst를 추가하면, 요청을 제출할 때 검증 오류가 있으면 그 페이지로 돌아가도록 할 수 있다.

``` java server pages
<form:errors path="description" cssClass="text-warning"/>
```
form에서 오류를 확인하려면 해당 라인을 추가한다.
