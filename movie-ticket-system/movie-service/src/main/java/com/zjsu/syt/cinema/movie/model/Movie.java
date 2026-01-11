package com.zjsu.syt.cinema.movie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Genre is required")
    @Column(nullable = false, length = 100)
    private String genre;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Column(nullable = false)
    private Integer duration;

    @Column(length = 100)
    private String director;

    @Column(length = 500)
    private String cast;

    @Column(length = 20)
    private String rating;

    @Column(name = "release_date")
    private java.time.LocalDate releaseDate;

    @Column(length = 500)
    private String posterUrl;

    @Column(columnDefinition = "DECIMAL(3,1) DEFAULT 0.0")
    private Double averageRating = 0.0;

    @Column(name = "is_showing", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isShowing = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Movie(String title, String description, String genre, Integer duration,
                String director, String cast, String rating, java.time.LocalDate releaseDate,
                String posterUrl) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
        this.cast = cast;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
    }
}
