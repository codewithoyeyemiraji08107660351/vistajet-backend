package com.vistajet.vistajet.news;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Integer> {

    Optional<News> findByTitleIgnoreCase(String title);
}
