package com.inksolutions.news_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsArticle {
    @JsonProperty("title")
    private String title;
    @JsonProperty("publishedAt")
    private String publishedAt;
    @JsonProperty("urlToImage")
    private String urlToImage;
    @JsonProperty("url")
    private String url;
}
