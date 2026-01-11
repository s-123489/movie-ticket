package com.zjsu.syt.cinema.recommendation.controller;

import com.zjsu.syt.cinema.recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);

    private final RecommendationService recommendationService;

    @Value("${server.port}")
    private String currentPort;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
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

    @GetMapping("/recommendations/user/{userId}")
    public Map<String, Object> getRecommendationsForUser(@PathVariable String userId) {
        log.info("Recommendation Service [port: {}, hostname: {}] getting recommendations for user: {}",
                currentPort, getHostname(), userId);

        List<Map<String, Object>> recommendations = recommendationService.getRecommendationsForUser(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", recommendations);
        response.put("count", recommendations.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/recommendations/genre/{genre}")
    public Map<String, Object> getMoviesByGenre(@PathVariable String genre) {
        log.info("Recommendation Service [port: {}, hostname: {}] getting movies by genre: {}",
                currentPort, getHostname(), genre);

        List<Map<String, Object>> movies = recommendationService.getMoviesByGenre(genre);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", movies);
        response.put("count", movies.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/recommendations/popular")
    public Map<String, Object> getPopularMovies() {
        log.info("Recommendation Service [port: {}, hostname: {}] getting popular movies",
                currentPort, getHostname());

        List<Map<String, Object>> movies = recommendationService.getPopularMovies();

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", movies);
        response.put("count", movies.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("port", currentPort);
        health.put("hostname", getHostname());
        health.put("service", "recommendation-service");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }
}
