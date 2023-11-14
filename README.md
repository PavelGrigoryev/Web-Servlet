# Web-servlet приложение для тренировки использования servlet-фильтров

## Автор: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

***

### Технологии, которые я использовал на проекте:

* Java 17
* Gradle 8.2
* Lombok plugin 8.4
* Servlet API 6.0.0
* Jooq plugin 8.2
* Postgresql 42.6.0
* HikariCP 5.0.1
* Gson 2.10.1
* Slf4j-API 2.0.7
* Logback logger 1.4.11
* SnakeYaml 2.1
* Liquibase plugin 2.2.0
* Mapstruct 1.5.3.Final
* JJwt 0.12.3

***

### Инструкция для запуска приложения локально:

1. У вас должна быть
   установлена [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Tomcat 10.1](https://tomcat.apache.org/download-10.cgi), [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   [Postgresql](https://www.postgresql.org/download/) (P.S: Postgresql можно развернуть в докере).
2. В Postgresql нужно создать базу данных. Как пример: "web" . Sql: CREATE DATABASE web
3. В [application.yaml](src/main/resources/application.yaml) в строчке №3 введите ваш username для Postgresql, в строчке
   №4 введите ваш password для Postgresql.
4. Так же для liquibase и jooq плагинов в [gradle.properties](gradle.properties) в строчке №2 введите ваш username для
   Postgresql, в строчке №3 введите ваш password для Postgresql.
5. В настройках идеи Run -> Edit Configurations... вы должны поставить Tomcat 10.1. И в графе Deployment
   очистить Application context.
6. При запуске приложения Liquibase сам создаст таблицы и наполнит их дефолтными значениями. Jooq сам сгенерирует
   сущности.
7. Приложение готово к использованию.

***

### Описание:

* Реализовано логирование входящих запросов целиком, причём password пользователя кодируется и заменяется на
  закодированный в [PasswordEncoderFilter](src/main/java/ru/clevertec/webservlet/filter/PasswordEncoderFilter.java)
  и уже в логах в закодированном виде. Если request параметров или request body нет, то они не выводятся в логе.
  Пример вывода лога:

````text
2023-11-14T20:23:27.767 [http-nio-8080-exec-3] INFO  r.c.webservlet.filter.LoggingFilter - 
 Request method: PUT
 Request URL: http://localhost:8080/users
 Request headers: 
  content-type: application/json
  authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJyb2xlcyI6WyJBRE1JTiIsIkJBTUJPTElBTE8iXSwic3ViIjoiQWxmaWUiLCJpYXQiOjE2OTk5ODI1ODcsImV4cCI6MTcwMDA2ODk4N30.WHRohu2DYfr1DrmFa7AY53emJpnoDPn3PS3GVjApmFYeTHtL5X7NdQLjrK6Sm8dS
  content-length: 56
  host: localhost:8080
  connection: Keep-Alive
  user-agent: Apache-HttpClient/4.5.14 (Java/17.0.9)
  cookie: JSESSIONID=C795BAAAD8FD549C27D5C58C725A5F10
  accept-encoding: br,deflate,gzip,x-gzip
 Request parameters:
  id: 2
 Request body:
  {"password":"asQJhq/Gr7s28OXzHA/SX6zOYLfaCNdy232cYpzcFjA\u003d","role_ids":[3]}
````

* Доступ к изменению ролей, просмотру и изменению пользователей только у пользователей с ролью ADMIN. За это отвечает
  [JwtFilter](src/main/java/ru/clevertec/webservlet/filter/JwtFilter.java). Остальные могут только просматривать роли.
  Чтоб получить роль ADMIN нужно:
    * Зарегистрироваться в [auth.http](src/main/resources/http/auth.http).
    * В role_ids указать id с номером 1 (это роль ADMIN).
    * Получить jwt и прикреплять его в header Authorization.
    * Jwt действителен в течение 24ох часов.
* Список ролей зашифрован в jwt вместе с nickname пользователя (пароля там нет xD).
* Если список ролей пользователя изменился, то при следующем запросе пользователе будет выброшен exception о том, что
  список его ролей изменился, будет создан новый jwt и ему будет предложено ввести его в header. Пример:

````json
{
  "exception": "ADMIN change your roles, your new jwt: 'Hi, i am a JWT example'  put it in header 'Authorization'"
}
````

* Если пользователь был удалён админом, его jwt станет не валидным и вылетит exception. Пример:

````json
{
  "exception": "Wrong Jwt! User with nickname Alfie was deleted by ADMIN"
}
````

***

### Http Запросы

* [auth.http](src/main/resources/http/auth.http) для авторизации
* [users.http](src/main/resources/http/users.http) для пользователей
* [roles.http](src/main/resources/http/roles.http) для ролей
* [env.json](src/main/resources/http/http-client.env.json) для актуального JWT

***
