package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.model.Article;
import com.inksolutions.news_aggregator.model.GuardianArticle;
import com.inksolutions.news_aggregator.model.GuardianResponse;
import com.inksolutions.news_aggregator.repository.ArticleRepository;
import com.inksolutions.news_aggregator.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuardianNewsService implements AggregationService {
    private final ArticleRepository articleRepository;
    private final HuggingFaceService openAIService;
    private final RestTemplate restTemplate;
    @Value("${guardian.api.key}")
    private String apiKey;

    @Override
    public void fetchArticles() {
        String url = "https://content.guardianapis.com/search?api-key=" + apiKey + "&page-size=10&show-fields=thumbnail";
        GuardianResponse response = restTemplate.getForObject(url, GuardianResponse.class);

        if (response != null && response.getResponse() != null &&
                response.getResponse().getResults() != null) {
            List<GuardianArticle> guardianArticles = response.getResponse().getResults();
            List<Article> articles = guardianArticles.stream().map(result -> {

                Article article = new Article();

                if (result.getWebTitle() != null) {
                    article.setTitle(result.getWebTitle());
                } else {
                    throw new IllegalArgumentException("Guardian article title is missing");
                }

                article.setImage(result.getFields() != null ?
                        result.getFields().getThumbnail() : null);
                article.setLink(result.getWebUrl());
                String location = openAIService.determineLocation(result.getWebTitle());
                article.setLocation(location);

                if (result.getWebPublicationDate() != null) {
                    article.setPublishDate(OffsetDateTime.parse(result.getWebPublicationDate()));
                } else {
                    throw new IllegalArgumentException("Guardian article publication" +
                            " date is missing");
                }

                return article;
            }).collect(Collectors.toList());

            for (Article article : articles) {
                Optional<Article> existingArticle = articleRepository
                        .findByTitle(article.getTitle());
                if (!existingArticle.isPresent()) {
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
