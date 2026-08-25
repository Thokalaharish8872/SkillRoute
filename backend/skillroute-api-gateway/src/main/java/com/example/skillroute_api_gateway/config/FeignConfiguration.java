package com.example.skillroute_api_gateway.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    public RequestInterceptor jwtTokenInterceptor() {
        return new JwtTokenInterceptor();
    }
}
