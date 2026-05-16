package com.yeoljeong.tripmate.ai.infrastructure.config;

import com.yeoljeong.tripmate.ai.domain.service.AlertMessageFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertDomainConfig {

    @Bean
    public AlertMessageFormatter alertMessageFormatter() {
        return new AlertMessageFormatter();
    }
}
