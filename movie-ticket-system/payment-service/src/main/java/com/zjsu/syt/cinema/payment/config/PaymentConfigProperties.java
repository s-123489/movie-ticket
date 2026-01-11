package com.zjsu.syt.cinema.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 支付服务动态配置属性类
 * 使用 @RefreshScope 实现配置动态刷新
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "payment")
public class PaymentConfigProperties {

    /**
     * 支付超时时间（分钟）
     */
    private Integer timeout = 30;

    /**
     * 退款处理时间（分钟）
     */
    private Integer refundProcessTime = 15;

    /**
     * 最大退款尝试次数
     */
    private Integer maxRefundAttempts = 3;

    /**
     * 是否启用支付通知
     */
    private Boolean notificationEnabled = true;

    /**
     * 支付通知消息
     */
    private String notificationMessage = "支付成功";
}
