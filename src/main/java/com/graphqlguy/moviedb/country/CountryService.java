package com.graphqlguy.moviedb.country;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CountryService {

    private static final String COUNTRY_QUERY = """
        query Country($code: ID!) {
          country(code: $code) {
            code
            name
            emoji
            capital
            currency
          }
        }
        """;

    private final HttpSyncGraphQlClient graphQlClient = HttpSyncGraphQlClient.create(
            RestClient.create("https://countries.trevorblades.com/"));

    private final Cache<String, Country> countryCache;

    public Country findByCode(String code) {
        return countryCache.get(code, this::fetchByCode);
    }

    private Country fetchByCode(String code) {
        return graphQlClient.document(COUNTRY_QUERY)
                .variable("code", code)
                .retrieveSync("country")
                .toEntity(Country.class);
    }
}