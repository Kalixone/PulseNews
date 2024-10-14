package com.inksolutions.news_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NewsResponse {
    @JsonProperty("articles")
    private List<NewsArticle> articles;
}
