package com.zjsu.syt.cinema.recommendation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "movie-service", fallback = MovieClientFallback.class)
public interface MovieClient {

    @GetMapping("/api/movies")
    Map<String, Object> getAllMovies(@RequestParam(required = false) String showing);

    @GetMapping("/api/movies/genre/{genre}")
    Map<String, Object> getMoviesByGenre(@PathVariable("genre") String genre);
}
