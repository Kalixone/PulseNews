package com.inksolutions.news_aggregator.service;

import com.inksolutions.news_aggregator.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface ArticleService {
    Page<Article> getFromLocation(Pageable pageable, String location);
    List<Article> findAll();
    Page<Article> notConnectedToUS(Pageable pageable);
    long count();
    Optional<Article> getRandom();
    Optional<Article> findById(Long id);
    List<Article> searcher(String word);
    List<Article> findByPublishDate(OffsetDateTime startOfDay);
}
