# notes

1. jdk15 issue
    - https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0
      jdk17 is minimum for using spring boot 3.x
    - security issues:
        - CVE-2016-1000027, Score: 9.8 on `org.springframework:spring-web:5.3.31`

# Tools and env which used:

- IDE and tools:
    - IntelliJ IDEA 2024.3.5
    - Spring Initializer
- Env:
    - Linux Ubuntu 22.04

# API:

- SecondMethod:
    - Is it Ok to serve stale data?
        - For the sake of simplicity we don't allow that.
        - however it's not too hard to do that. instead of simply `rw.lock()` we can `rw.tryLock()`
    - File lock machanism on diff platforms (win/linux)
        - `.lock` via `posix(?)` way
    - consistency upon whole file:
        - Well Because we're serving data by single key each time so it doesn't really matter but it's simple.
