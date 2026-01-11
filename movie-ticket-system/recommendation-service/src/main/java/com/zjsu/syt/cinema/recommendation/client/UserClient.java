package com.zjsu.syt.cinema.recommendation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}/history")
    Map<String, Object> getUserPurchaseHistory(@PathVariable("userId") String userId);
}
