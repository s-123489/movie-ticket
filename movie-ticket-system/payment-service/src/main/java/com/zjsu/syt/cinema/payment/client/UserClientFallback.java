package com.zjsu.syt.cinema.payment.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public Map<String, Object> addPurchaseHistory(String userId, Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "User service unavailable");
        return response;
    }
}
