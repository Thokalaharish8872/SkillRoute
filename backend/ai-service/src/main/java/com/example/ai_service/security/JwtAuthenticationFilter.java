package com.example.ai_service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        final String authHeader =
                request.getHeader("Authorization");


        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {


            filterChain.doFilter(request, response);

            return;
        }


        String jwtToken =
                authHeader.substring(7);

        if (SecurityContextHolder
                .getContext()
                .getAuthentication() != null) {

            filterChain.doFilter(request, response);

            return;
        }


        try {

            Claims claims =
                    jwtService.extractAllClaims(jwtToken);

            String userEmail =
                    claims.getSubject();

            logger.info("userEmail : {}", userEmail);

            if (userEmail != null &&
                    jwtService.validateToken(
                            claims,
                            userEmail)) {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(userEmail);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

                logger.info(
                        "User authenticated successfully: {}",
                        userEmail
                );
            }

        } catch (Exception e) {

            logger.error(
                    "JWT authentication failed",
                    e
            );
        }

        filterChain.doFilter(request, response);
    }
}