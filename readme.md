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

- Abstraction (Interface segregation)
    - Of course, we can create an interface for each service and implements them, but due to limited scope of this task
      I consider it an overkill.
    - And also testing is simple yet. Almost everything can be done with simple mocking.
    - So I'm gonna postpone this matter, unless there is actual second implementation of services, or testing gets hard.

- SecondMethod:
    - Is it Ok to serve stale data?
        - For the sake of simplicity we don't allow that.
        - however it's not too hard to do that. instead of simply `rw.lock()` we can `rw.tryLock()`
    - File lock machanism on diff platforms (win/linux)
        - `.lock` via `posix(?)` way
    - consistency upon whole file:
        - Well Because we're serving data by single key each time so it doesn't really matter but it's simple.

- ThirdMethod:
    - Implementations:
        1. simple read and write with locks: not performant: `writeValueNaive`
        2. cache last read values then update and flush it: doesn't consider external changes
        3. cache values, monitor for changes, invalid cache upon external change, then update and flush: looks good yet.
        4. enqueue writes, try to commit after we got the lock.
    - Filesystem sync and flushing considerations?

- Testing:
    - I just wrote simple tests for SecondService.
    - And also generally I'm avoiding Tests which needs Spring Contexts. They're heavy, running is time-consuming, and
      sometimes messy.
    - Tests I didn't write:
        - Services: Tests for each: todo
        - Controllers: WebMvcTest: No need yet.
        - Integration tests: postponed, 
