package com.zjsu.syt.cinema.ticket.client;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MovieClientFallback implements MovieClient {

    @Override
    public Map<String, Object> getMovie(String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }

    @Override
    public Map<String, Object> getShowtime(String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }

    @Override
    public Map<String, Object> reserveSeats(String id, int seats) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }

    @Override
    public Map<String, Object> releaseSeats(String id, int seats) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Movie service unavailable");
        return response;
    }
}
