package com.inksolutions.news_aggregator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String image;
    private String location;
    private String link;
    private OffsetDateTime publishDate;
}
