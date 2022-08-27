# WebChat
cz2397 Cailin Zhou \
For Intro to Java final project

## How to start
1. Open directory `\WebChat` as the IDEA project
2. Maven all dependencies in pom.xml (automatic for IDEA open project, but wait for minutes)
3. configure your MySQL database in `WebChat/src/main/application.properties` (is optional for just test)
```
spring.datasource.url=jdbc:mysql:///webchat?serverTimezone=UTC&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
```
4. Run `WebChat/src/main/java/com.cz2397.webchat/WebChatAppRunner`
5. See the site show in Console, or directly visit `localhost/8080`
6. Open multiple pages, and log in the same room with different username, to experience the chatroom.

