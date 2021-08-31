package com.luxoft.springdb.lab1.service;

import com.luxoft.springdb.lab1.dao.CountryNotFoundException;
import com.luxoft.springdb.lab1.dao.CountryRepository;
import com.luxoft.springdb.lab1.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {

    private final CountryRepository repo;

    @Autowired
    public CountryService(CountryRepository repo) {
        this.repo = repo;
    }

    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<>();
        repo.findAll().forEach(countries::add);
        return countries;
    }

    public Country getCountryByName(String name) throws CountryNotFoundException {
        Country countryByName = repo.findCountryByName(name);
        if (countryByName == null) {
            throw new CountryNotFoundException("Country not found in database");
        }
        return countryByName;
    }

    public void createCountry(Country country) {
        Country existingCountry = repo.findCountryByNameAndCodeName(country.getName(), country.getCodeName());
        if (existingCountry == null) {
            repo.save(country);
        }
    }

    public void deleteCountry(Country country) {
        Country countryForDelete = repo.findCountryByNameAndCodeName(country.getName(), country.getCodeName());
        repo.delete(countryForDelete);
    }

    public void updateCountry(Country oldCountry, Country newCountry) {
        oldCountry.setName(newCountry.getName());
        oldCountry.setCodeName(newCountry.getCodeName());
        repo.save(oldCountry);
    }
}
