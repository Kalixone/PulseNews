package com.inksolutions.news_aggregator.controller;

import com.inksolutions.news_aggregator.model.Article;
import com.inksolutions.news_aggregator.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public List<Article> getArticles() {
        return articleService.findAll();
    }

    @GetMapping("/location")
    public Page<Article> findByLocation(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam String location) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.getFromLocation(pageable, location);
    }

    @GetMapping("/not-connected")
    public Page<Article> notConnectedToUS(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleService.notConnectedToUS(pageable);
    }

    @GetMapping("/counter")
    public long count() {
        return articleService.count();
    }

    @GetMapping("/random")
    public Optional<Article> getRandom() {
       return articleService.getRandom();
    }

    @GetMapping("/search")
    public List<Article> searcher(@RequestParam String word) {
        return articleService.searcher(word);
    }

    @GetMapping("/date")
    public List<Article> findByPublishDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        OffsetDateTime offsetDateTime = localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        return articleService.findByPublishDate(offsetDateTime);
    }
}
