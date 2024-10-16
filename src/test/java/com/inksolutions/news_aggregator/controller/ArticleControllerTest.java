package com.inksolutions.news_aggregator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inksolutions.news_aggregator.model.Article;
import com.inksolutions.news_aggregator.repository.ArticleRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    private static MockMvc mockMvc;

    private static final String LOCATION = "Wales";
    private static final String KEYWORD = "First Article";
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime
            .of(2024, 10, 15, 10, 0, 0, 0, ZoneOffset.UTC);

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/articles/add-3-articles-to-articles_table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/articles/delete-articles-from-" +
                            "articles_table.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify findByLocation() returns articles for valid location")
    void findByLocation_ValidLocation_ReturnsArticlesPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/articles/location")
                        .param("location", LOCATION)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode content = root.path("content");

        List<Article> articles = objectMapper.readValue(content.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class));

        Assertions.assertNotNull(articles, "The article list should not be null.");
        Assertions.assertFalse(articles.isEmpty(), "The article list should not be empty.");
        Assertions.assertTrue(articles.stream()
                        .anyMatch(article -> article.getLocation().equals("Wales")),
                "Expected an article with location 'Wales'.");
    }

    @Test
    @DisplayName("Verify searcher() returns matching articles for valid keyword")
    void searcher_ValidKeyword_ReturnsMatchingArticles() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/articles/search?word=" + KEYWORD))
                .andExpect(status().isOk())
                .andReturn();

        List<Article> articles = objectMapper.readValue(result.getResponse()
                .getContentAsString(), objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Article.class));

        Assertions.assertTrue(articles.stream()
                .anyMatch(article -> article.getTitle().equals("First Article")));
    }

    @Test
    @DisplayName("Verify findByPublishDate() returns articles for valid date")
    void findByPublishDate_ValidDate_ReturnsArticlesWithinDateRange() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/articles/date")
                        .param("date", PUBLISH_DATE.toLocalDate().toString()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        List<Article> articles = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class)
        );

        Assertions.assertNotNull(articles, "Response should not be null.");
        Assertions.assertFalse(articles.isEmpty(), "Article list should not be empty.");
        Assertions.assertEquals(3, articles.size(), "There should be 3 articles.");
    }
}
