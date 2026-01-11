package com.zjsu.syt.cinema.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @PostMapping("/api/users/{userId}/purchase")
    Map<String, Object> addPurchaseHistory(@PathVariable("userId") String userId, @RequestBody Map<String, Object> request);
}
