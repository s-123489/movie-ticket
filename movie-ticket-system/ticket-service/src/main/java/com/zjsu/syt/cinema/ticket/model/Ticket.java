package com.zjsu.syt.cinema.ticket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotBlank(message = "Showtime ID is required")
    @Column(name = "showtime_id", nullable = false)
    private String showtimeId;

    @NotBlank(message = "Movie ID is required")
    @Column(name = "movie_id", nullable = false)
    private String movieId;

    @NotBlank(message = "Seat number is required")
    @Column(name = "seat_number", nullable = false, length = 20)
    private String seatNumber;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status = TicketStatus.RESERVED;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Ticket(String userId, String showtimeId, String movieId,
                 String seatNumber, Double price) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.bookingTime = LocalDateTime.now();
        this.expiryTime = LocalDateTime.now().plusMinutes(15);
    }
}
