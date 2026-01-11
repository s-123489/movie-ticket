package com.zjsu.syt.cinema.movie.repository;

import com.zjsu.syt.cinema.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByIsShowingTrue();
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    Optional<Movie> findByTitle(String title);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
