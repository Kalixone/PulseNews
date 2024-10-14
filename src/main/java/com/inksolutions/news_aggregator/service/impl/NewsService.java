package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.model.Article;
import com.inksolutions.news_aggregator.model.NewsResponse;
import com.inksolutions.news_aggregator.repository.ArticleRepository;
import com.inksolutions.news_aggregator.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService implements AggregationService {
    private final ArticleRepository articleRepository;
    private final HuggingFaceService openAIService;
    private final RestTemplate restTemplate;
    @Value("${news.api.key}")
    private String apiKey;

    @Override
    public void fetchArticles() {
        String url = "https://newsapi.org/v2/top-headlines?country=us&pageSize=100&page=1&apiKey=" + apiKey;
        NewsResponse response = restTemplate.getForObject(url, NewsResponse.class);

        if (response != null && response.getArticles() != null) {
            List<Article> articles = response.getArticles().stream().map(result -> {
                Article article = new Article();
                article.setTitle(result.getTitle());
                article.setLink(result.getUrl());
                if (result.getPublishedAt() != null) {
                    article.setPublishDate(OffsetDateTime.parse(result.getPublishedAt()));
                } else {
                    article.setPublishDate(null);
                }

                article.setImage(result.getUrlToImage());
                String location = openAIService.determineLocation(result.getTitle());
                article.setLocation(location);

                return article;
            }).collect(Collectors.toList());

            for (Article article : articles) {
                if (!articleRepository.findByTitle(article.getTitle()).isPresent()) {
                    articleRepository.save(article);
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledFetchArticles() {
        fetchArticles();
    }
}
