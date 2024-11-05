package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.model.City;
import com.inksolutions.news_aggregator.repository.CityRepository;
import com.inksolutions.news_aggregator.service.DataLoaderService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class DataLoaderImpl implements DataLoaderService, CommandLineRunner {
    private final CityRepository cityRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void run(String... args) throws Exception {
        loadCity();
    }

    @Override
    public void loadCity() {
        if (cityRepository.count() > 0) {
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(12);
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/cities.csv")))) {
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                if (record[3] == null || record[3].isEmpty()) {
                    throw new IllegalArgumentException("City name is missing");
                }

                final String cityName = record[3];

                executorService.submit(() -> {
                    EntityManager entityManager = entityManagerFactory.createEntityManager();
                    try {
                        City city = new City();
                        city.setName(cityName);
                        entityManager.getTransaction().begin();
                        entityManager.persist(city);
                        entityManager.getTransaction().commit();
                    } catch (Exception e) {
                        if (entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().rollback();
                        }
                        e.printStackTrace();
                    } finally {
                        entityManager.close();
                    }
                });
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }
}
