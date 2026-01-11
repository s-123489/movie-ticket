package com.zjsu.syt.cinema.user.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 支付消息消费者
 * 监听支付相关的消息队列，处理支付事件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    /**
     * 消费支付成功消息
     */
    @RabbitListener(queues = "cinema.payment.success.queue")
    public void handlePaymentSuccess(PaymentEvent event) {
        try {
            log.info("收到支付事件消息: eventType={}, paymentId={}, userId={}, amount={}",
                    event.getEventType(), event.getPaymentId(), event.getUserId(), event.getAmount());

            switch (event.getEventType()) {
                case PAYMENT_SUCCESS -> handlePaymentSuccessEvent(event);
                case PAYMENT_FAILED -> handlePaymentFailedEvent(event);
                default -> log.warn("未知的支付事件类型: {}", event.getEventType());
            }

            log.info("支付事件处理完成: paymentId={}", event.getPaymentId());
        } catch (Exception e) {
            log.error("处理支付事件失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 消费退款消息
     */
    @RabbitListener(queues = "cinema.refund.queue")
    public void handleRefund(PaymentEvent event) {
        try {
            log.info("收到退款事件消息: eventType={}, paymentId={}, userId={}, amount={}",
                    event.getEventType(), event.getPaymentId(), event.getUserId(), event.getAmount());

            switch (event.getEventType()) {
                case REFUND_REQUESTED -> handleRefundRequestedEvent(event);
                case REFUND_COMPLETED -> handleRefundCompletedEvent(event);
                case REFUND_FAILED -> handleRefundFailedEvent(event);
                default -> log.warn("未知的退款事件类型: {}", event.getEventType());
            }

            log.info("退款事件处理完成: paymentId={}", event.getPaymentId());
        } catch (Exception e) {
            log.error("处理退款事件失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 处理支付成功事件
     */
    private void handlePaymentSuccessEvent(PaymentEvent event) {
        log.info("处理支付成功事件: 用户 {} 支付成功，金额 {}, {}",
                event.getUserId(), event.getAmount(), event.getDescription());

        // 这里可以添加业务逻辑，例如：
        // - 发送支付成功通知给用户
        // - 更新用户积分
        // - 记录用户消费行为
        // - 触发推荐系统更新
    }

    /**
     * 处理支付失败事件
     */
    private void handlePaymentFailedEvent(PaymentEvent event) {
        log.warn("处理支付失败事件: 用户 {} 支付失败，{}",
                event.getUserId(), event.getDescription());

        // 这里可以添加业务逻辑，例如：
        // - 发送支付失败通知给用户
        // - 记录失败原因
    }

    /**
     * 处理退款请求事件
     */
    private void handleRefundRequestedEvent(PaymentEvent event) {
        log.info("处理退款请求事件: 用户 {} 申请退款，金额 {}, {}",
                event.getUserId(), event.getAmount(), event.getDescription());

        // 这里可以添加业务逻辑，例如：
        // - 发送退款申请确认通知
        // - 更新用户退款记录
    }

    /**
     * 处理退款完成事件
     */
    private void handleRefundCompletedEvent(PaymentEvent event) {
        log.info("处理退款完成事件: 用户 {} 退款完成，金额 {}",
                event.getUserId(), event.getAmount());

        // 这里可以添加业务逻辑，例如：
        // - 发送退款完成通知给用户
        // - 更新用户账户余额
        // - 减少用户积分
    }

    /**
     * 处理退款失败事件
     */
    private void handleRefundFailedEvent(PaymentEvent event) {
        log.warn("处理退款失败事件: 用户 {} 退款失败，{}",
                event.getUserId(), event.getDescription());

        // 这里可以添加业务逻辑，例如：
        // - 发送退款失败通知给用户
        // - 记录失败原因
    }
}
