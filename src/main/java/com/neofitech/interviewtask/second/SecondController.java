package com.neofitech.interviewtask.second;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecondController {

    private final SecondService secondService;

    @GetMapping("getValue/{key}")
    public ResponseEntity<SecondDto> getValue(@PathVariable("key") String key) {
        return secondService.getValue(key);
    }
}
