package com.zjsu.syt.cinema.ticket.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "movie-service", fallback = MovieClientFallback.class)
public interface MovieClient {

    @GetMapping("/api/movies/{id}")
    Map<String, Object> getMovie(@PathVariable("id") String id);

    @GetMapping("/api/showtimes/{id}")
    Map<String, Object> getShowtime(@PathVariable("id") String id);

    @PostMapping("/api/showtimes/{id}/reserve")
    Map<String, Object> reserveSeats(@PathVariable("id") String id, @RequestParam int seats);

    @PostMapping("/api/showtimes/{id}/release")
    Map<String, Object> releaseSeats(@PathVariable("id") String id, @RequestParam int seats);
}
