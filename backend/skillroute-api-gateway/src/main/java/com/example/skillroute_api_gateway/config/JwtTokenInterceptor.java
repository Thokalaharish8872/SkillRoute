package com.example.skillroute_api_gateway.config;

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
            
            logger.info("Feign request to: {}", template.url());
            logger.info("Original Authorization header present: {}", authorizationHeader != null && !authorizationHeader.isEmpty());
            
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                template.header("Authorization", authorizationHeader);
                logger.info("Forwarded Authorization header to downstream service");
            } else {
                logger.warn("No Authorization header found in incoming request");
            }
        } else {
            logger.warn("No request context available for Feign request to: {}", template.url());
        }
    }
}
