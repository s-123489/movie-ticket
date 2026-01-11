package com.zjsu.syt.cinema.movie.controller;

import com.zjsu.syt.cinema.movie.model.Movie;
import com.zjsu.syt.cinema.movie.model.Showtime;
import com.zjsu.syt.cinema.movie.service.MovieService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MovieController {

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;

    @Value("${server.port}")
    private String currentPort;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
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

    @PostMapping("/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createMovie(@Valid @RequestBody MovieRequest request) {
        log.info("Movie Service [port: {}, hostname: {}] creating movie: {}",
                currentPort, getHostname(), request.title());

        Movie movie = new Movie(
                request.title(),
                request.description(),
                request.genre(),
                request.duration(),
                request.director(),
                request.cast(),
                request.rating(),
                request.releaseDate(),
                request.posterUrl()
        );
        Movie created = movieService.createMovie(movie);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", MovieResponse.from(created));
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/movies")
    public Map<String, Object> getAllMovies(@RequestParam(required = false) String showing) {
        log.info("Movie Service [port: {}, hostname: {}] getting movies (showing: {})",
                currentPort, getHostname(), showing);

        List<Movie> movies;
        if ("true".equalsIgnoreCase(showing)) {
            movies = movieService.getShowingMovies();
        } else {
            movies = movieService.getAllMovies();
        }

        List<MovieResponse> movieResponses = movies.stream()
                .map(MovieResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", movieResponses);
        response.put("count", movieResponses.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Map<String, Object>> getMovieById(@PathVariable String id) {
        log.info("Movie Service [port: {}, hostname: {}] getting movie by id: {}",
                currentPort, getHostname(), id);

        return movieService.getMovieById(id)
                .map(movie -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", MovieResponse.from(movie));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "Movie with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping("/movies/search")
    public Map<String, Object> searchMovies(@RequestParam String query) {
        log.info("Movie Service [port: {}, hostname: {}] searching movies: {}",
                currentPort, getHostname(), query);

        List<MovieResponse> movies = movieService.searchMoviesByTitle(query).stream()
                .map(MovieResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", movies);
        response.put("count", movies.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/movies/genre/{genre}")
    public Map<String, Object> getMoviesByGenre(@PathVariable String genre) {
        log.info("Movie Service [port: {}, hostname: {}] getting movies by genre: {}",
                currentPort, getHostname(), genre);

        List<MovieResponse> movies = movieService.getMoviesByGenre(genre).stream()
                .map(MovieResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", movies);
        response.put("count", movies.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Map<String, Object>> updateMovie(
            @PathVariable String id,
            @Valid @RequestBody MovieRequest request) {
        log.info("Movie Service [port: {}, hostname: {}] updating movie: {}",
                currentPort, getHostname(), id);

        return movieService.getMovieById(id)
                .map(existing -> {
                    existing.setTitle(request.title());
                    existing.setDescription(request.description());
                    existing.setGenre(request.genre());
                    existing.setDuration(request.duration());
                    existing.setDirector(request.director());
                    existing.setCast(request.cast());
                    existing.setRating(request.rating());
                    existing.setReleaseDate(request.releaseDate());
                    existing.setPosterUrl(request.posterUrl());
                    if (request.isShowing() != null) {
                        existing.setIsShowing(request.isShowing());
                    }
                    Movie updated = movieService.updateMovie(existing);

                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", MovieResponse.from(updated));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "Movie with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @DeleteMapping("/movies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable String id) {
        log.info("Movie Service [port: {}, hostname: {}] deleting movie: {}",
                currentPort, getHostname(), id);
        movieService.deleteMovie(id);
    }

    @PostMapping("/showtimes")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createShowtime(@Valid @RequestBody ShowtimeRequest request) {
        log.info("Movie Service [port: {}, hostname: {}] creating showtime for movie: {}",
                currentPort, getHostname(), request.movieId());

        Showtime showtime = new Showtime(
                request.movieId(),
                request.showtime(),
                request.theater(),
                request.totalSeats(),
                request.price()
        );
        Showtime created = movieService.createShowtime(showtime);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", ShowtimeResponse.from(created));
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/showtimes")
    public Map<String, Object> getAllShowtimes() {
        log.info("Movie Service [port: {}, hostname: {}] getting all showtimes",
                currentPort, getHostname());

        List<ShowtimeResponse> showtimes = movieService.getAllShowtimes().stream()
                .map(ShowtimeResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", showtimes);
        response.put("count", showtimes.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/showtimes/movie/{movieId}")
    public Map<String, Object> getShowtimesByMovie(@PathVariable String movieId) {
        log.info("Movie Service [port: {}, hostname: {}] getting showtimes for movie: {}",
                currentPort, getHostname(), movieId);

        List<ShowtimeResponse> showtimes = movieService.getShowtimesByMovie(movieId).stream()
                .map(ShowtimeResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", showtimes);
        response.put("count", showtimes.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<Map<String, Object>> getShowtimeById(@PathVariable String id) {
        log.info("Movie Service [port: {}, hostname: {}] getting showtime by id: {}",
                currentPort, getHostname(), id);

        return movieService.getShowtimeById(id)
                .map(showtime -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", ShowtimeResponse.from(showtime));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "Showtime with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @PostMapping("/showtimes/{id}/reserve")
    public ResponseEntity<Map<String, Object>> reserveSeats(
            @PathVariable String id,
            @RequestParam int seats) {
        log.info("Movie Service [port: {}, hostname: {}] reserving {} seats for showtime: {}",
                currentPort, getHostname(), seats, id);

        try {
            movieService.decreaseAvailableSeats(id, seats);
            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("status", "SUCCESS");
            response.put("message", "Seats reserved successfully");
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

    @PostMapping("/showtimes/{id}/release")
    public ResponseEntity<Map<String, Object>> releaseSeats(
            @PathVariable String id,
            @RequestParam int seats) {
        log.info("Movie Service [port: {}, hostname: {}] releasing {} seats for showtime: {}",
                currentPort, getHostname(), seats, id);

        try {
            movieService.increaseAvailableSeats(id, seats);
            Map<String, Object> response = new HashMap<>();
            response.put("port", currentPort);
            response.put("hostname", getHostname());
            response.put("status", "SUCCESS");
            response.put("message", "Seats released successfully");
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
        health.put("service", "movie-service");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }

    record MovieRequest(
            String title,
            String description,
            String genre,
            Integer duration,
            String director,
            String cast,
            String rating,
            java.time.LocalDate releaseDate,
            String posterUrl,
            Boolean isShowing
    ) {}

    record MovieResponse(
            String id,
            String title,
            String description,
            String genre,
            Integer duration,
            String director,
            String cast,
            String rating,
            String releaseDate,
            String posterUrl,
            Double averageRating,
            Boolean isShowing,
            String createdAt
    ) {
        static MovieResponse from(Movie movie) {
            return new MovieResponse(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDescription(),
                    movie.getGenre(),
                    movie.getDuration(),
                    movie.getDirector(),
                    movie.getCast(),
                    movie.getRating(),
                    movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : null,
                    movie.getPosterUrl(),
                    movie.getAverageRating(),
                    movie.getIsShowing(),
                    movie.getCreatedAt() != null ? movie.getCreatedAt().toString() : null
            );
        }
    }

    record ShowtimeRequest(
            String movieId,
            LocalDateTime showtime,
            String theater,
            Integer totalSeats,
            Double price
    ) {}

    record ShowtimeResponse(
            String id,
            String movieId,
            String showtime,
            String theater,
            Integer totalSeats,
            Integer availableSeats,
            Double price,
            Boolean isActive,
            String createdAt
    ) {
        static ShowtimeResponse from(Showtime showtime) {
            return new ShowtimeResponse(
                    showtime.getId(),
                    showtime.getMovieId(),
                    showtime.getShowtime() != null ? showtime.getShowtime().toString() : null,
                    showtime.getTheater(),
                    showtime.getTotalSeats(),
                    showtime.getAvailableSeats(),
                    showtime.getPrice(),
                    showtime.getIsActive(),
                    showtime.getCreatedAt() != null ? showtime.getCreatedAt().toString() : null
            );
        }
    }
}
