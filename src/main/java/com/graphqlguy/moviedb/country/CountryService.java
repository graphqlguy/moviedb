package com.graphqlguy.moviedb.country;

import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
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

    public Country findByCode(String code) {
        return graphQlClient.document(COUNTRY_QUERY)
                .variable("code", code)
                .retrieveSync("country")
                .toEntity(Country.class);
    }
}