# Study-WebFlux_3
web flux 

# 사용 framework/db/library

H2 Database

Spring R2DBC(비동기 지원 DB)

Lombok

Spring Boot DevTools

Spring Reactive web(Spring WebFlux) -netty



Webflux 는 Asynchronous Non-blocking I/O 을 방식을 활용하여 성능을 끌어 올릴 수 있는 장점이 있다. 

그런데 이 말은 즉, Non Blocking 기반으로 코드를 작성해야 한다. 

만약 Blocking 코드가 있다면 Webflux를 사용하는 의미가 떨어지게 된다. 

얼마 전까지는 Java 진영에 Non Blocking 을 지원하는 DB Driver가 없었지만, 최근에 R2DBC 가 릴리즈되어 

이제는 Java 에서도 Non Blocking 으로 DB 를 접근할 수 있게 되었다.

# 참고 링크
 1. R2DBC Sample 
    https://spring.io/projects/spring-data-r2dbc#overview
 2. WebFlux Sample
    https://howtodoinjava.com/spring-webflux/spring-webflux-tutorial/
    
