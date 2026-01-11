package com.zjsu.syt.cinema.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 * 定义交换机、队列和绑定关系
 */
@Configuration
public class RabbitMQConfig {

    // 支付相关常量
    public static final String PAYMENT_EXCHANGE = "cinema.payment.exchange";
    public static final String PAYMENT_SUCCESS_QUEUE = "cinema.payment.success.queue";
    public static final String PAYMENT_SUCCESS_ROUTING_KEY = "payment.success";

    // 退款相关常量
    public static final String REFUND_QUEUE = "cinema.refund.queue";
    public static final String REFUND_ROUTING_KEY = "payment.refund";

    /**
     * 创建支付交换机（Topic类型）
     */
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE, true, false);
    }

    /**
     * 创建支付成功队列
     */
    @Bean
    public Queue paymentSuccessQueue() {
        return QueueBuilder.durable(PAYMENT_SUCCESS_QUEUE)
                .withArgument("x-message-ttl", 86400000) // 消息TTL 24小时
                .build();
    }

    /**
     * 创建退款队列
     */
    @Bean
    public Queue refundQueue() {
        return QueueBuilder.durable(REFUND_QUEUE)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    /**
     * 绑定支付成功队列到交换机
     */
    @Bean
    public Binding paymentSuccessBinding(Queue paymentSuccessQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentSuccessQueue)
                .to(paymentExchange)
                .with(PAYMENT_SUCCESS_ROUTING_KEY);
    }

    /**
     * 绑定退款队列到交换机
     */
    @Bean
    public Binding refundBinding(Queue refundQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(refundQueue)
                .to(paymentExchange)
                .with(REFUND_ROUTING_KEY);
    }

    /**
     * 消息转换器 - 使用JSON格式
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
