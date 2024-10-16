package com.inksolutions.news_aggregator.repository;

import com.inksolutions.news_aggregator.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByTitle(String title);

    Page<Article> findByLocation(Pageable pageable, String location);

    @Query("SELECT a FROM Article a WHERE a.location IS NULL")
    Page<Article> notConnectedToUS(Pageable pageable);

    List<Article> findByTitleContainingIgnoreCase(String word);

    List<Article> findByPublishDateBetween(OffsetDateTime startOfDay, OffsetDateTime endOfDay);
}

