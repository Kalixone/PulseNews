package com.inksolutions.news_aggregator.service;

import org.springframework.stereotype.Service;

@Service
public interface AggregationService {
    void fetchArticles();
    void scheduledFetchArticles();
}
