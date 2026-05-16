package com.yeoljeong.tripmate.ai.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    // 각 adapter에서 필요한 baseUrl 붙여서 사용
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
