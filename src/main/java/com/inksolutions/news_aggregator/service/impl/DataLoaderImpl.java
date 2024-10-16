package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.model.City;
import com.inksolutions.news_aggregator.repository.CityRepository;
import com.inksolutions.news_aggregator.service.DataLoaderService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class DataLoaderImpl implements DataLoaderService, CommandLineRunner {
    private final CityRepository cityRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCity();
    }

    @Override
    public void loadCity() {
        if (cityRepository.count() > 0) {
            return;
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(getClass()
                .getResourceAsStream("/cities.csv")))) {
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                if (record[3] == null || record[3].isEmpty()) {
                    throw new IllegalArgumentException("City name is missing");
                }

                City city = new City();
                city.setName(record[3]);
                cityRepository.save(city);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
