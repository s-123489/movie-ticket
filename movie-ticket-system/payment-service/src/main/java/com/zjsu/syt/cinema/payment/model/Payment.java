package com.zjsu.syt.cinema.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotBlank(message = "Ticket ID is required")
    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Min(value = 0, message = "Amount must be non-negative")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public Payment(String userId, String ticketId, Double amount, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}
