package com.zjsu.syt.cinema.payment.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付事件消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件类型
     */
    private PaymentEventType eventType;

    /**
     * 支付ID
     */
    private Long paymentId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额
     */
    private Double amount;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 支付事件类型枚举
     */
    public enum PaymentEventType {
        PAYMENT_SUCCESS,    // 支付成功
        PAYMENT_FAILED,     // 支付失败
        REFUND_REQUESTED,   // 退款请求
        REFUND_COMPLETED,   // 退款完成
        REFUND_FAILED       // 退款失败
    }
}
