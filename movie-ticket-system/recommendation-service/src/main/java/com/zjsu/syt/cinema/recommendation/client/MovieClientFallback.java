package com.zjsu.syt.cinema.recommendation.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MovieClientFallback implements MovieClient {

    @Override
    public Map<String, Object> getAllMovies(String showing) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }

    @Override
    public Map<String, Object> getMoviesByGenre(String genre) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }
}
