package com.zjsu.syt.cinema.payment.service;

import com.zjsu.syt.cinema.payment.client.TicketClient;
import com.zjsu.syt.cinema.payment.client.UserClient;
import com.zjsu.syt.cinema.payment.messaging.PaymentEvent;
import com.zjsu.syt.cinema.payment.messaging.PaymentMessageProducer;
import com.zjsu.syt.cinema.payment.model.*;
import com.zjsu.syt.cinema.payment.repository.PaymentRepository;
import com.zjsu.syt.cinema.payment.repository.RefundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final TicketClient ticketClient;
    private final UserClient userClient;
    private final PaymentMessageProducer messageProducer;

    public PaymentService(PaymentRepository paymentRepository, RefundRepository refundRepository,
                         TicketClient ticketClient, UserClient userClient,
                         PaymentMessageProducer messageProducer) {
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.ticketClient = ticketClient;
        this.userClient = userClient;
        this.messageProducer = messageProducer;
    }

    @Transactional
    public Payment processPayment(String userId, String ticketId, Double amount, PaymentMethod paymentMethod, Map<String, Object> ticketDetails) {
        Payment payment = new Payment(userId, ticketId, amount, paymentMethod);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setProcessedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        Map<String, Object> confirmResponse = ticketClient.confirmPayment(ticketId, savedPayment.getId());
        if ("ERROR".equals(confirmResponse.get("status"))) {
            throw new RuntimeException("Failed to confirm ticket payment");
        }

        Map<String, Object> purchaseRequest = new HashMap<>();
        purchaseRequest.put("ticketId", ticketId);
        purchaseRequest.put("movieTitle", ticketDetails.get("movieTitle"));
        purchaseRequest.put("showtime", ticketDetails.get("showtime"));
        purchaseRequest.put("seatNumber", ticketDetails.get("seatNumber"));
        purchaseRequest.put("price", amount);
        purchaseRequest.put("paymentId", savedPayment.getId());

        userClient.addPurchaseHistory(userId, purchaseRequest);

        // 发送支付成功消息
        try {
            PaymentEvent event = new PaymentEvent(
                    PaymentEvent.PaymentEventType.PAYMENT_SUCCESS,
                    Long.parseLong(savedPayment.getId()),
                    Long.parseLong(ticketId),
                    Long.parseLong(userId),
                    amount,
                    paymentMethod.name(),
                    LocalDateTime.now(),
                    "支付成功，订单号: " + savedPayment.getTransactionId()
            );
            messageProducer.sendPaymentEvent(event);
            log.info("支付成功事件已发送: paymentId={}, userId={}", savedPayment.getId(), userId);
        } catch (Exception e) {
            log.error("发送支付成功事件失败: {}", e.getMessage(), e);
        }

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getPaymentsByUser(String userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Payment> getPaymentByTicketId(String ticketId) {
        return paymentRepository.findByTicketId(ticketId);
    }

    @Transactional
    public Refund requestRefund(String paymentId, String ticketId, Double amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Payment is not in COMPLETED status");
        }

        Refund refund = new Refund(paymentId, ticketId, amount, reason);
        refund.setStatus(RefundStatus.APPROVED);
        Refund savedRefund = refundRepository.save(refund);

        ticketClient.cancelTicket(ticketId);

        // 发送退款请求消息
        try {
            PaymentEvent event = new PaymentEvent(
                    PaymentEvent.PaymentEventType.REFUND_REQUESTED,
                    Long.parseLong(paymentId),
                    Long.parseLong(ticketId),
                    Long.parseLong(payment.getUserId()),
                    amount,
                    payment.getPaymentMethod().name(),
                    LocalDateTime.now(),
                    "退款原因: " + reason
            );
            messageProducer.sendPaymentEvent(event);
            log.info("退款请求事件已发送: refundId={}, paymentId={}", savedRefund.getId(), paymentId);
        } catch (Exception e) {
            log.error("发送退款请求事件失败: {}", e.getMessage(), e);
        }

        return savedRefund;
    }

    @Transactional
    public Refund processRefund(String refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        if (refund.getStatus() != RefundStatus.APPROVED) {
            throw new RuntimeException("Refund is not approved");
        }

        refund.setStatus(RefundStatus.COMPLETED);
        refund.setProcessedAt(LocalDateTime.now());

        Payment payment = paymentRepository.findById(refund.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        // 发送退款完成消息
        try {
            PaymentEvent event = new PaymentEvent(
                    PaymentEvent.PaymentEventType.REFUND_COMPLETED,
                    Long.parseLong(refund.getPaymentId()),
                    Long.parseLong(refund.getTicketId()),
                    Long.parseLong(payment.getUserId()),
                    refund.getAmount(),
                    payment.getPaymentMethod().name(),
                    LocalDateTime.now(),
                    "退款已完成"
            );
            messageProducer.sendPaymentEvent(event);
            log.info("退款完成事件已发送: refundId={}, paymentId={}", refund.getId(), refund.getPaymentId());
        } catch (Exception e) {
            log.error("发送退款完成事件失败: {}", e.getMessage(), e);
        }

        return refundRepository.save(refund);
    }

    public List<Refund> getRefundsByPayment(String paymentId) {
        return refundRepository.findByPaymentId(paymentId);
    }
}
