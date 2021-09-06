package com.luxoft.springdb.lab1.dao;

import com.luxoft.springdb.lab1.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class CountryDao {
    private static final String LOAD_COUNTRIES_SQL = "insert into country (name, code_name) values (?,?)";

    private static final String GET_ALL_COUNTRIES_SQL = "select * from country";
    private static final String GET_COUNTRIES_BY_NAME_SQL = "select * from country where name like :name";
    private static final String GET_COUNTRY_BY_NAME_SQL = "select * from country where name = :name";
    private static final String GET_COUNTRY_BY_CODE_NAME_SQL = "select * from country where code_name = :codeName";

    private static final String UPDATE_COUNTRY_NAME_SQL = "update country SET name=:name where code_name=:codeName";

    public static final String[][] COUNTRY_INIT_DATA = {{"Australia", "AU"},
            {"Canada", "CA"}, {"France", "FR"}, {"Hong Kong", "HK"},
            {"Iceland", "IC"}, {"Japan", "JP"}, {"Nepal", "NP"},
            {"Russian Federation", "RU"}, {"Sweden", "SE"},
            {"Switzerland", "CH"}, {"United Kingdom", "GB"},
            {"United States", "US"}};

    private static final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcTemplate jdbc;

    @Autowired
    public CountryDao(NamedParameterJdbcTemplate jdbcTemplate, JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbc = jdbc;
    }

    public List<Country> getCountryList() {
        return jdbcTemplate.query(GET_ALL_COUNTRIES_SQL, COUNTRY_ROW_MAPPER);
    }

    public List<Country> getCountryListStartWith(String name) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
                "name", name + "%");
        return jdbcTemplate.query(GET_COUNTRIES_BY_NAME_SQL, sqlParameterSource, COUNTRY_ROW_MAPPER);
    }

    public void updateCountryName(String codeName, String newCountryName) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", newCountryName);
        parameters.put("codeName", codeName);
        jdbcTemplate.update(UPDATE_COUNTRY_NAME_SQL, parameters);
    }

    public void loadCountries() {
        List<Object[]> data = Arrays.asList(COUNTRY_INIT_DATA);
        jdbc.batchUpdate(LOAD_COUNTRIES_SQL, data);
    }

    public Country getCountryByCodeName(String codeName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("codeName", codeName);
        return jdbcTemplate.query(GET_COUNTRY_BY_CODE_NAME_SQL, params, COUNTRY_ROW_MAPPER).get(0);
    }

    public Country getCountryByName(String name) throws CountryNotFoundException {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        Country country = jdbcTemplate.queryForObject(GET_COUNTRY_BY_NAME_SQL, params, COUNTRY_ROW_MAPPER);
        if (country == null) {
            throw new CountryNotFoundException("Country not found");
        }
        return country;
    }
}
