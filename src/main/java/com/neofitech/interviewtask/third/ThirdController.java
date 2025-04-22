package com.neofitech.interviewtask.third;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ThirdController {

    private final ThirdService thirdService;

    @GetMapping("setValue/{key}/{value}")
    public ResponseEntity<?> getValue(@PathVariable String key, @PathVariable String value) {
        return thirdService.setValue(key, value);
    }
}
