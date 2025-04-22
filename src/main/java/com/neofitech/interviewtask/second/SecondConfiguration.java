package com.neofitech.interviewtask.second;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecondConfiguration {
    @Bean
    public ConfigReader configReader() {
        return new ConfigReader();
    }
}
