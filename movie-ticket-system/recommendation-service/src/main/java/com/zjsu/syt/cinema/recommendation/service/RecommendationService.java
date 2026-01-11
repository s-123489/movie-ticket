package com.zjsu.syt.cinema.recommendation.service;

import com.zjsu.syt.cinema.recommendation.client.MovieClient;
import com.zjsu.syt.cinema.recommendation.client.UserClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final MovieClient movieClient;
    private final UserClient userClient;

    public RecommendationService(MovieClient movieClient, UserClient userClient) {
        this.movieClient = movieClient;
        this.userClient = userClient;
    }

    public List<Map<String, Object>> getRecommendationsForUser(String userId) {
        Map<String, Object> historyResponse = userClient.getUserPurchaseHistory(userId);

        if ("ERROR".equals(historyResponse.get("status"))) {
            return getPopularMovies();
        }

        List<Map<String, Object>> history = (List<Map<String, Object>>) historyResponse.get("data");
        if (history == null || history.isEmpty()) {
            return getPopularMovies();
        }

        Map<String, Integer> genreCount = new HashMap<>();
        for (Map<String, Object> purchase : history) {
            String movieTitle = (String) purchase.get("movieTitle");
        }

        Map<String, Object> moviesResponse = movieClient.getAllMovies("true");
        if ("ERROR".equals(moviesResponse.get("status"))) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> movies = (List<Map<String, Object>>) moviesResponse.get("data");
        if (movies == null || movies.size() <= 5) {
            return movies;
        }

        Collections.shuffle(movies);
        return movies.stream().limit(5).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getMoviesByGenre(String genre) {
        Map<String, Object> moviesResponse = movieClient.getMoviesByGenre(genre);

        if ("ERROR".equals(moviesResponse.get("status"))) {
            return Collections.emptyList();
        }

        return (List<Map<String, Object>>) moviesResponse.get("data");
    }

    public List<Map<String, Object>> getPopularMovies() {
        Map<String, Object> moviesResponse = movieClient.getAllMovies("true");

        if ("ERROR".equals(moviesResponse.get("status"))) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> movies = (List<Map<String, Object>>) moviesResponse.get("data");
        if (movies == null) {
            return Collections.emptyList();
        }

        return movies.stream()
                .sorted((m1, m2) -> {
                    Double rating1 = (Double) m1.getOrDefault("averageRating", 0.0);
                    Double rating2 = (Double) m2.getOrDefault("averageRating", 0.0);
                    return rating2.compareTo(rating1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
}
