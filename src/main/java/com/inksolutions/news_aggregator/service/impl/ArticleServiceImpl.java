package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.dto.ArticleDTO;
import com.inksolutions.news_aggregator.repository.ArticleRepository;
import com.inksolutions.news_aggregator.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final Random random;

    @Override
    public Page<ArticleDTO> getFromLocation(Pageable pageable, String location) {
        return articleRepository.findByLocation(pageable, location).map(ArticleDTO::fromEntity);
    }

    @Override
    public List<ArticleDTO> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ArticleDTO> notConnectedToUS(Pageable pageable) {
        return articleRepository.notConnectedToUS(pageable).map(ArticleDTO::fromEntity);
    }

    @Override
    public Optional<ArticleDTO> findById(Long id) {
        return articleRepository.findById(id).map(ArticleDTO::fromEntity);
    }

    @Override
    public List<ArticleDTO> searcher(String word) {
        return articleRepository.findByTitleContainingIgnoreCase(word).stream()
                .map(ArticleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> findByPublishDate(OffsetDateTime date) {
        OffsetDateTime startOfDay = date.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        return articleRepository.findByPublishDateBetween(startOfDay, endOfDay).stream()
                .map(ArticleDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
