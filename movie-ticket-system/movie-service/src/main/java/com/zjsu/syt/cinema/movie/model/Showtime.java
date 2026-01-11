package com.zjsu.syt.cinema.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Movie ID is required")
    @Column(name = "movie_id", nullable = false)
    private String movieId;

    @NotNull(message = "Showtime is required")
    @Column(name = "showtime", nullable = false)
    private LocalDateTime showtime;

    @NotBlank(message = "Theater is required")
    @Column(nullable = false, length = 100)
    private String theater;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Showtime(String movieId, LocalDateTime showtime, String theater,
                   Integer totalSeats, Double price) {
        this.movieId = movieId;
        this.showtime = showtime;
        this.theater = theater;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.price = price;
    }
}
