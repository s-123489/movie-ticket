package com.zjsu.syt.cinema.movie.service;

import com.zjsu.syt.cinema.movie.model.Movie;
import com.zjsu.syt.cinema.movie.model.Showtime;
import com.zjsu.syt.cinema.movie.repository.MovieRepository;
import com.zjsu.syt.cinema.movie.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieService(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getShowingMovies() {
        return movieRepository.findByIsShowingTrue();
    }

    public Optional<Movie> getMovieById(String id) {
        return movieRepository.findById(id);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    public Movie updateMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }

    public Showtime createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public List<Showtime> getShowtimesByMovie(String movieId) {
        return showtimeRepository.findByMovieIdAndIsActiveTrueOrderByShowtimeAsc(movieId);
    }

    public List<Showtime> getShowtimesByDateRange(LocalDateTime start, LocalDateTime end) {
        return showtimeRepository.findByShowtimeBetweenAndIsActiveTrue(start, end);
    }

    public Optional<Showtime> getShowtimeById(String id) {
        return showtimeRepository.findById(id);
    }

    public Showtime updateShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    @Transactional
    public void decreaseAvailableSeats(String showtimeId, int count) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        if (showtime.getAvailableSeats() < count) {
            throw new RuntimeException("Not enough available seats");
        }

        showtime.setAvailableSeats(showtime.getAvailableSeats() - count);
        showtimeRepository.save(showtime);
    }

    @Transactional
    public void increaseAvailableSeats(String showtimeId, int count) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        showtime.setAvailableSeats(showtime.getAvailableSeats() + count);
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(String id) {
        showtimeRepository.deleteById(id);
    }
}
