package com.example.search_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.super.secret.key}")
    private String SECRETKEY;


    public Claims extractAllClaims(String token) {

        logger.info("Parsing JWT");

        JwtParserBuilder builder = Jwts.parser();

        builder.verifyWith((SecretKey) getSecretKey());

        JwtParser jwtParser = builder.build();

        try {

            Jws<Claims> jws =
                    jwtParser.parseSignedClaims(token);

            Claims claims = jws.getPayload();

            logger.info("claims : {}", claims);

            return claims;

        } catch (Exception e) {

            logger.error("JWT parsing failed", e);

            throw new IllegalArgumentException(
                    "Invalid JWT token", e
            );
        }
    }


    public boolean validateToken(Claims claims, String email) {

        String extractedEmail = claims.getSubject();

        Date expiration = claims.getExpiration();

        return extractedEmail != null
                && extractedEmail.equals(email)
                && expiration != null
                && !expiration.before(new Date());
    }


    private Key getSecretKey() {

        byte[] keyBytes;

        try {

            keyBytes =
                    Decoders.BASE64.decode(SECRETKEY);

        } catch (Exception e) {

            keyBytes =
                    SECRETKEY.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}