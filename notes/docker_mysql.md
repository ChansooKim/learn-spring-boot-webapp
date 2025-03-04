# Docker를 통한 MySQL 사용
***
> Docker Desktop을 사용하길 권장한다.
- Docker Desktop을 이용하면, Mac, Windows, Linux에서 손쉽게 컨테이너를
빌드/공유/실행 할 수 있다.

[Install Docker Desktop on Mac](https://docs.docker.com/desktop/setup/install/mac-install/)
** Windows의 경우, 반드시 관리자 계정에서 설치해야 한다.

설치 후 cmd에서 `docker --version`명령어를 통해 정상 설치 유무를 확인할 수 있다.
```
Docker version 27.5.1, build 9f9e405
```


Docker Desktop 실행 후
아래 명령어 실행
``` cmd
docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=todos-user --env MYSQL_PASSWORD=dummytodos --env MYSQL_DATABASE=todos --name mysql --publish 3306:3306 mysql:8-oracle
```
mysql:8-oracle -> Docker Image
root password: dummypassword
user: todos-user
password: dummytodos
database: todos
port: 3306

완료 후
`docker container ls`를 통해 존재하는 모든 컨테이너를 확인할 수 있다.

보통 개발용 PC에 뭔가를 설치하려 할 때 대개 Docker를 사용한다.
-> 즉시 컨테이너를 삭제할 수 있기 때문
로컬 PC에 설치하면 복잡해진다.

mysql 의존성 추가
``` xml
<dependency>  
    <groupId>com.mysql</groupId>  
    <artifactId>mysql-connector-j</artifactId>  
</dependency>
```

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/todos  
spring.datasource.username=todos-user  
spring.datasource.password=dummytodos  
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

MySQL Shell 설치
https://dev.mysql.com/doc/mysql-shell/8.0/en/mysqlsh.html

> MySQL 구동을 위해 Shell을 설치하는 방법도 있지만
> 그냥 command에서 명령어를 통해 확인해볼 수 있다.

`docker ps` 실행중인 컨테이너 목록 조회
`docker exec -it mysql mysql -u root -p` mysql 컨테이너 내부로 진입
``` cmd
docker exec -it <컨테이너_이름_or_ID> mysql -u root -p
```
`mysql> ` 가 나타나면 접속이 완료된 것

`mysql> use todos` 사용할 스키마 선택
Database changed가 나타나면 정상 변경

`mysql> select * from todo;`
+----+--------------------+------------+-------------+-------------+
| id | description        | done       | target_date | username    |
+----+--------------------+------------+-------------+-------------+
|  1 | Learn to use MySQL | 0x00       | 2026-03-04  | in28minutes |
+----+--------------------+------------+-------------+-------------+
1 row in set (0.00 sec)

값을 확인할 수 있다.