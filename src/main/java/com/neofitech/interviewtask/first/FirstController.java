package com.neofitech.interviewtask.first;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class FirstController {

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/getNext")
    public FirstDto firstMethod() {
        return new FirstDto(counter.incrementAndGet());
    }



}
