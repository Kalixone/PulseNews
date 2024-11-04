package com.inksolutions.news_aggregator.dto;

import com.inksolutions.news_aggregator.model.Article;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ArticleDTO(
        Long id,
        @NotNull
        String title,
        String image,
        String location,
        String link,
        @NotNull
        OffsetDateTime publishDate
) {
    public static ArticleDTO fromEntity(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getImage(),
                article.getLocation(),
                article.getLink(),
                article.getPublishDate()
        );
    }
}
