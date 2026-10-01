package com.example.user_details_service.testing;

import io.jsonwebtoken.Claims;

import java.util.*;

public class DefaultClaims implements Claims {
    @Override
    public String getIssuer() {
        return "";
    }

    @Override
    public String getSubject() {
        return "";
    }

    @Override
    public Set<String> getAudience() {
        return Set.of();
    }

    @Override
    public Date getExpiration() {
        return null;
    }

    @Override
    public Date getNotBefore() {
        return null;
    }

    @Override
    public Date getIssuedAt() {
        return null;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public <T> T get(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return Set.of();
    }

    @Override
    public Collection<Object> values() {
        return List.of();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return Set.of();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
