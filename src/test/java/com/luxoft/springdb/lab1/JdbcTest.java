package com.luxoft.springdb.lab1;

import com.luxoft.springdb.lab1.dao.CountryRepository;
import com.luxoft.springdb.lab1.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JdbcTest {

    @Autowired
    private CountryRepository countryDao;

    public final String[][] COUNTRY_INIT_DATA = {{"Australia", "AU"},
            {"Canada", "CA"}, {"France", "FR"}, {"Hong Kong", "HK"},
            {"Iceland", "IC"}, {"Japan", "JP"}, {"Nepal", "NP"},
            {"Russian Federation", "RU"}, {"Sweden", "SE"},
            {"Switzerland", "CH"}, {"United Kingdom", "GB"},
            {"United States", "US"}};

    private final List<Country> expectedCountryList = new ArrayList<Country>();
    private final List<com.luxoft.springdb.lab1.model.Country> expectedCountryListStartsWithA = new ArrayList<Country>();
    private final Country countryWithChangedName = new Country("Russia", "RU");

    @BeforeEach
    public void setUp() throws Exception {
        initExpectedCountryLists();
        for (String[] country : COUNTRY_INIT_DATA) {
            Country c = new Country(country[0], country[1]);
            countryDao.save(c);
        }
    }


    @Test
    @DirtiesContext
    public void testCountryList() {
        List<Country> countryList = (List<Country>) countryDao.findAll();
        assertNotNull(countryList);
        assertEquals(expectedCountryList.size(), countryList.size());
        for (int i = 0; i < expectedCountryList.size(); i++) {
            assertEquals(expectedCountryList.get(i), countryList.get(i));
        }
    }

    @Test
    @DirtiesContext
    public void testCountryListStartsWithA() {
        List<Country> countryList = countryDao.findAllByNameStartingWith("A");
        assertNotNull(countryList);
        assertEquals(expectedCountryListStartsWithA.size(), countryList.size());
        for (int i = 0; i < expectedCountryListStartsWithA.size(); i++) {
            assertEquals(expectedCountryListStartsWithA.get(i), countryList.get(i));
        }
    }

    @Test
    @DirtiesContext
    public void testCountryChange() {
        Country ru = countryDao.findCountryByCodeName("RU");
        ru.setName("Russia");
        countryDao.save(ru);
        assertEquals(countryWithChangedName, countryDao.findCountryByCodeName("RU"));
    }

    private void initExpectedCountryLists() {
        for (String[] countryInitData : COUNTRY_INIT_DATA) {
            Country country = new Country(countryInitData[0], countryInitData[1]);
            expectedCountryList.add(country);
            if (country.getName().startsWith("A")) {
                expectedCountryListStartsWithA.add(country);
            }
        }
    }
}