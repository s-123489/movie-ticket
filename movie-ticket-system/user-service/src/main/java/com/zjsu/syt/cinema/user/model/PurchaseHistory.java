package com.zjsu.syt.cinema.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_history")
@Data
@NoArgsConstructor
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(name = "movie_title", nullable = false)
    private String movieTitle;

    @Column(name = "showtime")
    private LocalDateTime showtime;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    @Column(name = "payment_id")
    private String paymentId;

    @CreationTimestamp
    @Column(name = "purchased_at", nullable = false, updatable = false)
    private LocalDateTime purchasedAt;

    public PurchaseHistory(String userId, String ticketId, String movieTitle,
                          LocalDateTime showtime, String seatNumber, Double price, String paymentId) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.movieTitle = movieTitle;
        this.showtime = showtime;
        this.seatNumber = seatNumber;
        this.price = price;
        this.paymentId = paymentId;
    }
}
