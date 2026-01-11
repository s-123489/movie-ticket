package com.zjsu.syt.cinema.payment.controller;

import com.zjsu.syt.cinema.payment.model.Payment;
import com.zjsu.syt.cinema.payment.model.PaymentMethod;
import com.zjsu.syt.cinema.payment.model.Refund;
import com.zjsu.syt.cinema.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Value("${server.port}")
    private String currentPort;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private String getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null && !hostname.isEmpty()) {
            return hostname;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Failed to get hostname: {}", e.getMessage());
        }
        return "unknown-" + currentPort;
    }

    @PostMapping("/payments/process")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        log.info("Payment Service [port: {}, hostname: {}] processing payment for user: {}",
                currentPort, getHostname(), request.userId());

        try {
            Payment payment = paymentService.processPayment(
                    request.userId(),
                    request.ticketId(),
                    request.amount(),
                    request.paymentMethod(),
                    request.ticketDetails()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("data", PaymentResponse.from(payment));
            response.put("status", "SUCCESS");
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/payments")
    public Map<String, Object> getAllPayments() {
        log.info("Payment Service [port: {}, hostname: {}] getting all payments",
                currentPort, getHostname());

        List<PaymentResponse> payments = paymentService.getAllPayments().stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", payments);
        response.put("count", payments.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Map<String, Object>> getPaymentById(@PathVariable String id) {
        log.info("Payment Service [port: {}, hostname: {}] getting payment by id: {}",
                currentPort, getHostname(), id);

        return paymentService.getPaymentById(id)
                .map(payment -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", PaymentResponse.from(payment));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "Payment with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping("/payments/user/{userId}")
    public Map<String, Object> getPaymentsByUser(@PathVariable String userId) {
        log.info("Payment Service [port: {}, hostname: {}] getting payments for user: {}",
                currentPort, getHostname(), userId);

        List<PaymentResponse> payments = paymentService.getPaymentsByUser(userId).stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", payments);
        response.put("count", payments.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @PostMapping("/refunds/request")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> requestRefund(@Valid @RequestBody RefundRequest request) {
        log.info("Payment Service [port: {}, hostname: {}] requesting refund for payment: {}",
                currentPort, getHostname(), request.paymentId());

        try {
            Refund refund = paymentService.requestRefund(
                    request.paymentId(),
                    request.ticketId(),
                    request.amount(),
                    request.reason()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("data", RefundResponse.from(refund));
            response.put("status", "SUCCESS");
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/refunds/{id}/process")
    public ResponseEntity<Map<String, Object>> processRefund(@PathVariable String id) {
        log.info("Payment Service [port: {}, hostname: {}] processing refund: {}",
                currentPort, getHostname(), id);

        try {
            Refund refund = paymentService.processRefund(id);
            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("data", RefundResponse.from(refund));
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("port", currentPort);
            errorResponse.put("hostname", getHostname());
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("port", currentPort);
        health.put("hostname", getHostname());
        health.put("service", "payment-service");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }

    record ProcessPaymentRequest(
            String userId,
            String ticketId,
            Double amount,
            PaymentMethod paymentMethod,
            Map<String, Object> ticketDetails
    ) {}

    record PaymentResponse(
            String id,
            String userId,
            String ticketId,
            Double amount,
            String paymentMethod,
            String status,
            String transactionId,
            String createdAt,
            String processedAt
    ) {
        static PaymentResponse from(Payment payment) {
            return new PaymentResponse(
                    payment.getId(),
                    payment.getUserId(),
                    payment.getTicketId(),
                    payment.getAmount(),
                    payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null,
                    payment.getStatus() != null ? payment.getStatus().name() : null,
                    payment.getTransactionId(),
                    payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : null,
                    payment.getProcessedAt() != null ? payment.getProcessedAt().toString() : null
            );
        }
    }

    record RefundRequest(
            String paymentId,
            String ticketId,
            Double amount,
            String reason
    ) {}

    record RefundResponse(
            String id,
            String paymentId,
            String ticketId,
            Double amount,
            String reason,
            String status,
            String createdAt,
            String processedAt
    ) {
        static RefundResponse from(Refund refund) {
            return new RefundResponse(
                    refund.getId(),
                    refund.getPaymentId(),
                    refund.getTicketId(),
                    refund.getAmount(),
                    refund.getReason(),
                    refund.getStatus() != null ? refund.getStatus().name() : null,
                    refund.getCreatedAt() != null ? refund.getCreatedAt().toString() : null,
                    refund.getProcessedAt() != null ? refund.getProcessedAt().toString() : null
            );
        }
    }
}
