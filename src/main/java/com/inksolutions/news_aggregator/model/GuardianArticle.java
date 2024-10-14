package com.inksolutions.news_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuardianArticle {
    @JsonProperty("webTitle")
    private String webTitle;
    @JsonProperty("webPublicationDate")
    private String webPublicationDate;
    @JsonProperty("webUrl")
    private String webUrl;
    @JsonProperty("fields")
    private Fields fields;

    @Getter
    @Setter
    public static class Fields {
        @JsonProperty("thumbnail")
        private String thumbnail;
    }
}
