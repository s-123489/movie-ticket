package com.zjsu.syt.cinema.movie.repository;

import com.zjsu.syt.cinema.movie.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, String> {
    List<Showtime> findByMovieIdAndIsActiveTrueOrderByShowtimeAsc(String movieId);
    List<Showtime> findByShowtimeBetweenAndIsActiveTrue(LocalDateTime start, LocalDateTime end);
    List<Showtime> findByMovieId(String movieId);
    List<Showtime> findByTheater(String theater);
}
