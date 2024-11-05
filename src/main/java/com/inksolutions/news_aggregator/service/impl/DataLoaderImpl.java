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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<City> citiesBatch = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("/cities.csv")))) {
            String[] record;
            int batchSize = 100;

            while ((record = csvReader.readNext()) != null) {
                if (record[3] == null || record[3].isEmpty()) {
                    throw new IllegalArgumentException("City name is missing");
                }

                City city = new City();
                city.setName(record[3]);
                citiesBatch.add(city);


                if (citiesBatch.size() == batchSize) {

                    List<City> batchToSave = new ArrayList<>(citiesBatch);
                    executorService.submit(() -> {
                        cityRepository.saveAll(batchToSave);
                    });
                    citiesBatch.clear();
                }
            }


            if (!citiesBatch.isEmpty()) {
                List<City> batchToSave = new ArrayList<>(citiesBatch);
                executorService.submit(() -> {
                    cityRepository.saveAll(batchToSave);
                });
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }
}