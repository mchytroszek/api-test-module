package com.example.api.base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Helper that provides pre-configured REST Assured request specifications.
 * All methods return new instances — safe for parallel test execution.
 */
public final class RequestHelper {

    private RequestHelper() {
        // Utility class — no instantiation
    }

    /**
     * Returns a base request specification pointed at the JSONPlaceholder API.
     */
    public static RequestSpecification given() {
        return RestAssured.given()
                .baseUri(ApiConstants.BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    /**
     * Performs a GET request to the given path and returns the response.
     */
    public static Response get(String path) {
        return given().when().get(path);
    }

    /**
     * Performs a GET request with query parameters.
     */
    public static Response get(String path, Map<String, Object> queryParams) {
        return given().queryParams(queryParams).when().get(path);
    }

    /**
     * Performs a POST request with a JSON body (as a Map).
     */
    public static Response post(String path, Map<String, Object> body) {
        return given().body(body).when().post(path);
    }

    /**
     * Performs a DELETE request.
     */
    public static Response delete(String path) {
        return given().when().delete(path);
    }
}