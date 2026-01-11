package com.zjsu.syt.cinema.payment.controller;

import com.zjsu.syt.cinema.payment.config.PaymentConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理控制器
 * 用于演示Nacos Config动态刷新功能
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final PaymentConfigProperties configProperties;

    /**
     * 获取当前配置信息
     * 可以通过修改Nacos配置中心的配置来动态刷新这些值
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("timeout", configProperties.getTimeout());
        config.put("refundProcessTime", configProperties.getRefundProcessTime());
        config.put("maxRefundAttempts", configProperties.getMaxRefundAttempts());
        config.put("notificationEnabled", configProperties.getNotificationEnabled());
        config.put("notificationMessage", configProperties.getNotificationMessage());
        config.put("description", "这些配置可以在Nacos配置中心动态修改并实时生效");
        return config;
    }
}
