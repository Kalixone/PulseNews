package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.model.Article;
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

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final Random random;

    @Override
    public Page<Article> getFromLocation(Pageable pageable, String location) {
        return articleRepository.findByLocation(pageable, location);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Page<Article> notConnectedToUS(Pageable pageable) {
        return articleRepository.notConnectedToUS(pageable);
    }

    @Override
    public long count() {
        return articleRepository.count();
    }

    @Override
    public Optional<Article> getRandom() {
        long count = articleRepository.count();
        long randomNumber = random.nextInt(0, (int) count);
         return articleRepository.findById(randomNumber);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<Article> searcher(String word) {
        return articleRepository.findByTitleContainingIgnoreCase(word);
    }

    @Override
    public List<Article> findByPublishDate(OffsetDateTime date) {
        OffsetDateTime startOfDay = date.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        return articleRepository.findByPublishDateBetween(startOfDay, endOfDay);
    }
}
