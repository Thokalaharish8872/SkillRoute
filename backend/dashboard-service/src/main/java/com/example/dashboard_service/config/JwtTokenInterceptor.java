package com.example.dashboard_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                template.header("Authorization", authorizationHeader);
            }
        }
    }
}
