package com.inksolutions.news_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GuardianResponse {
    @JsonProperty("response")
    private Response response;

    @Getter
    @Setter
    public static class Response {
        @JsonProperty("results")
        private List<GuardianArticle> results;
    }
}
