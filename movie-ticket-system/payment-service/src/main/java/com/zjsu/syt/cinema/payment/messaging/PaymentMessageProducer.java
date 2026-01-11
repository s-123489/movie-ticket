package com.zjsu.syt.cinema.payment.messaging;

import com.zjsu.syt.cinema.payment.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * 支付消息生产者
 * 负责发送支付相关的异步消息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送支付事件消息
     */
    public void sendPaymentEvent(PaymentEvent event) {
        try {
            String routingKey = determineRoutingKey(event.getEventType());

            log.info("发送支付事件消息: eventType={}, paymentId={}, userId={}, orderId={}",
                    event.getEventType(), event.getPaymentId(), event.getUserId(), event.getOrderId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    routingKey,
                    event
            );

            log.info("支付事件消息发送成功");
        } catch (Exception e) {
            log.error("发送支付事件消息失败: {}", e.getMessage(), e);
            throw new RuntimeException("发送支付事件消息失败", e);
        }
    }

    /**
     * 根据事件类型确定路由键
     */
    private String determineRoutingKey(PaymentEvent.PaymentEventType eventType) {
        return switch (eventType) {
            case PAYMENT_SUCCESS, PAYMENT_FAILED -> RabbitMQConfig.PAYMENT_SUCCESS_ROUTING_KEY;
            case REFUND_REQUESTED, REFUND_COMPLETED, REFUND_FAILED -> RabbitMQConfig.REFUND_ROUTING_KEY;
        };
    }
}
