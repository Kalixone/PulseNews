package com.inksolutions.news_aggregator.repository;

import com.inksolutions.news_aggregator.model.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {
    private static final String TITLE = "First Article";
    private static final String LOCATION = "Barrow";
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime
            .of(2024, 10, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("Find article by valid title")
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql",
            "classpath:database/articles/add-3-articles-to-articles_table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindByTitle_ValidTitle_ReturnsArticle() {
        Optional<Article> article = articleRepository.findByTitle(TITLE);

        assertThat(article).isPresent();

        assertThat(article.get().getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Find articles by valid location")
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql",
            "classpath:database/articles/add-3-articles-to-articles_table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindByLocation_ValidLocation_ReturnsArticlesPage() {
        Pageable pageable = PageRequest.of(0,1);
        Page<Article> byLocation = articleRepository.findByLocation(pageable, LOCATION);

        boolean hasExpectedLocation  = byLocation.stream()
                .anyMatch(article -> article.getLocation()
                        .equals(LOCATION));

        assertThat(hasExpectedLocation).isTrue();
    }

    @Test
    @DisplayName("Find articles by publish date within valid date range")
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql",
            "classpath:database/articles/add-3-articles-to-articles_table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/articles/delete-articles-from-articles_table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindByPublishDate_ValidDateRange_ReturnsArticles() {
        OffsetDateTime startOfDay = PUBLISH_DATE.toLocalDate()
                .atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = PUBLISH_DATE.toLocalDate()
                .atTime(23, 59, 59, 0)
                .atOffset(ZoneOffset.UTC);

        List<Article> byPublishDateBetween = articleRepository
                .findByPublishDateBetween(startOfDay, endOfDay);

        assertThat(byPublishDateBetween).isNotEmpty();

        for (Article article : byPublishDateBetween) {
            assertThat(article.getPublishDate()).isBetween(startOfDay, endOfDay);
        }
    }
}
