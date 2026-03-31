package com.example.api.base;

/**
 * Shared constants for API tests.
 */
public final class ApiConstants {

    // Base URL for the public JSONPlaceholder test API
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Endpoint paths
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String USERS_ENDPOINT = "/users";
    public static final String COMMENTS_ENDPOINT = "/comments";
    public static final String TODOS_ENDPOINT = "/todos";

    // Test groups
    public static final String GROUP_SMOKE = "smoke";
    public static final String GROUP_SANITY = "sanity";
    public static final String GROUP_REGRESSION = "regression";

    // HTTP status codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NOT_FOUND = 404;

    private ApiConstants() {
        // Utility class — no instantiation
    }
}