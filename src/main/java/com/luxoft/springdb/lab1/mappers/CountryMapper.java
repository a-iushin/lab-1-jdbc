package com.luxoft.springdb.lab1.mappers;

import com.luxoft.springdb.lab1.dao.CountryRepository;
import com.luxoft.springdb.lab1.dto.CountryDto;
import com.luxoft.springdb.lab1.model.Country;
import org.springframework.stereotype.Service;

@Service
public class CountryMapper {

    private final CountryRepository repo;

    public CountryMapper(CountryRepository repo){
        this.repo = repo;
    }

    public CountryDto countryToCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setName(country.getName());
        dto.setCodeName(country.getCodeName());
        return dto;
    }

    public Country countryDTOToCountry(CountryDto dto) {
        return new Country(dto.getName(), dto.getCodeName());
    }
}
