package com.inksolutions.news_aggregator.service;

import com.inksolutions.news_aggregator.dto.ArticleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface ArticleService {
    Page<ArticleDTO> getFromLocation(Pageable pageable, String location);

    List<ArticleDTO> findAll();

    Page<ArticleDTO> notConnectedToUS(Pageable pageable);

    Optional<ArticleDTO> findById(Long id);

    List<ArticleDTO> searcher(String word);

    List<ArticleDTO> findByPublishDate(OffsetDateTime startOfDay);
}
