package com.inksolutions.news_aggregator.service;

import com.inksolutions.news_aggregator.model.Article;
import com.inksolutions.news_aggregator.repository.ArticleRepository;
import com.inksolutions.news_aggregator.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    private static final Long ARTICLE_ID = 1L;
    private static final String TITLE = "Random Title";
    private static final String IMAGE = "https://random.article.pl";
    private static final String LOCATION = "Wales";
    private static final String CITY_NOT_CONNECTED_TO_US = "Mrągowo";
    private static final String LINK = "https://random.link.pl";
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime
            .of(2024, 10, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("Get articles from specified location")
    public void getFromLocation_ValidLocation_ReturnsArticlesPage() {
        Article article = createArticle();
        Pageable pageable = PageRequest.of(0, 10);

        when(articleRepository.findByLocation(pageable, LOCATION))
                .thenReturn(new PageImpl<>(List.of(article), pageable, 1));

        Page<Article> searchedLocations = articleService.getFromLocation(pageable, LOCATION);

        assertThat(searchedLocations).containsExactly(article);
        assertThat(searchedLocations.getTotalElements()).isEqualTo(1);
        verify(articleRepository).findByLocation(pageable, LOCATION);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    @DisplayName("Fetch articles not connected to the US")
    public void notConnectedToUS_CityNotConnected_ReturnsArticlesNotConnected() {
        Article article = createArticle();
        article.setLocation(CITY_NOT_CONNECTED_TO_US);
        Pageable pageable = PageRequest.of(0, 10);

        when(articleRepository.notConnectedToUS(pageable))
                .thenReturn(new PageImpl<>(List.of(article), pageable, 1));

        Page<Article> articles = articleService.notConnectedToUS(pageable);

        assertThat(articles.getContent()).containsExactly(article);
        assertThat(articles.getTotalElements()).isEqualTo(1);
        verify(articleRepository).notConnectedToUS(pageable);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    @DisplayName("Retrieve article by valid ID")
    public void findById_ValidId_ReturnsExistingArticle() {
        Article article = createArticle();

        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.of(article));

        Optional<Article> articleById = articleService.findById(ARTICLE_ID);

        assertThat(articleById).isPresent();
        assertThat(articleById.get()).isEqualTo(article);
        verify(articleRepository).findById(ARTICLE_ID);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    @DisplayName("Search articles by keyword")
    public void searcher_ValidKeyword_ReturnsMatchingArticles() {
        Article article = createArticle();
        String searchKeyword = "Random";

        when(articleRepository.findByTitleContainingIgnoreCase(searchKeyword))
                .thenReturn(List.of(article));

        List<Article> searchedArticles = articleService.searcher(searchKeyword);

        assertThat(searchedArticles).containsExactly(article);
        verify(articleRepository).findByTitleContainingIgnoreCase(searchKeyword);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    @DisplayName("Find articles published on specific date")
    public void findByPublishDate_ValidDate_ReturnsArticlesWithinDateRange() {
        Article article = createArticle();

        OffsetDateTime publishDate = OffsetDateTime.of(2024, 10, 15, 10, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime startOfDay = publishDate.toLocalDate()
                .atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = publishDate.toLocalDate()
                .atTime(23, 59, 59, 0)
                .atOffset(ZoneOffset.UTC);

        when(articleRepository.findByPublishDateBetween(startOfDay, endOfDay))
                .thenReturn(List.of(article));

        List<Article> articles = articleService.findByPublishDate(publishDate);

        assertThat(articles).containsExactly(article);
        verify(articleRepository).findByPublishDateBetween(startOfDay, endOfDay);
        verifyNoMoreInteractions(articleRepository);
    }

    public Article createArticle() {
        Article article = new Article();
        article.setId(ARTICLE_ID);
        article.setTitle(TITLE);
        article.setImage(IMAGE);
        article.setLocation(LOCATION);
        article.setLink(LINK);
        article.setPublishDate(PUBLISH_DATE);
        return article;
    }
}
