package com.inksolutions.news_aggregator.repository;

import com.inksolutions.news_aggregator.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    boolean existsByName(String name);
}
