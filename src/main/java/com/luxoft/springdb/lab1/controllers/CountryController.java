package com.luxoft.springdb.lab1.controllers;

import com.luxoft.springdb.lab1.dao.CountryNotFoundException;
import com.luxoft.springdb.lab1.dto.CountryDto;
import com.luxoft.springdb.lab1.mappers.CountryMapper;
import com.luxoft.springdb.lab1.model.Country;
import com.luxoft.springdb.lab1.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Transactional
public class CountryController {

    private final CountryService service;
    private final CountryMapper mapper;

    @Autowired
    public CountryController(CountryService service, CountryMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/getCountries")
    public ResponseEntity<List<CountryDto>> getCountries() {
        List<Country> allCountries = service.getAllCountries();
        return new ResponseEntity<>(allCountries
                .stream()
                .map(mapper::countryToCountryDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getByName")
    public ResponseEntity<CountryDto> getCountryByName(@RequestParam String name) throws CountryNotFoundException {
        Country country = service.getCountryByName(name);
        return new ResponseEntity<>(mapper.countryToCountryDto(country), HttpStatus.OK);
    }


    @PostMapping("/createCountry")
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto dto) {
        Country country = mapper.countryDTOToCountry(dto);
        service.createCountry(country);
        return new ResponseEntity<>(mapper.countryToCountryDto(country), HttpStatus.CREATED);
    }

    @PutMapping("/updateCountryByName")
    public ResponseEntity<CountryDto> updateCountry(@RequestParam String name,
                                                    @RequestBody CountryDto updatedDto) throws CountryNotFoundException {
        Country oldCountry = service.getCountryByName(name);
        service.updateCountry(oldCountry, mapper.countryDTOToCountry(updatedDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteCountry")
    public ResponseEntity<CountryDto> deleteCountry(@RequestBody CountryDto dto) {
        service.deleteCountry(mapper.countryDTOToCountry(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

