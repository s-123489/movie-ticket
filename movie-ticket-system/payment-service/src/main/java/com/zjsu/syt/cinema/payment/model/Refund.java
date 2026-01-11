package com.zjsu.syt.cinema.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Data
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double amount;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefundStatus status = RefundStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public Refund(String paymentId, String ticketId, Double amount, String reason) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.reason = reason;
    }
}
