package com.luxoft.springdb.lab1.dao;

import com.luxoft.springdb.lab1.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

    List<Country> findAllByNameStartingWith(String starts);

    Country findCountryByName(String name);

    Country findCountryByCodeName(String codeName);

    Country findCountryByNameAndCodeName(String name, String codeName);
}
