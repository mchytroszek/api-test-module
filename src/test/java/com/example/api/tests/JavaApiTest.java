package com.example.api.tests;

import com.example.api.base.ApiConstants;
import com.example.api.base.BaseApiTest;
import com.example.api.base.RequestHelper;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * API tests written in Java.
 * Demonstrates standard Java patterns: verbose but explicit and readable.
 * All mutable state is kept in local variables — safe for parallel execution.
 */
public class JavaApiTest extends BaseApiTest {

    @Test(groups = {ApiConstants.GROUP_SMOKE},
            description = "GET /posts returns HTTP 200 and a non-empty list")
    public void getAllPostsReturnsOk() {
        log.info("Fetching all posts");

        Response response = RequestHelper.get(ApiConstants.POSTS_ENDPOINT);

        response.then()
                .statusCode(ApiConstants.HTTP_OK)
                .body("size()", greaterThan(0));

        log.info("Received {} posts", response.jsonPath().getList("$").size());
    }

    @Test(groups = {ApiConstants.GROUP_SMOKE},
            description = "GET /posts/1 returns the expected post with correct fields")
    public void getPostByIdReturnsCorrectPost() {
        log.info("Fetching post with id=1");

        Response response = RequestHelper.get(ApiConstants.POSTS_ENDPOINT + "/1");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, "Status code");
        soft.assertEquals(response.jsonPath().getInt("id"), 1, "Post id");
        soft.assertNotNull(response.jsonPath().getString("title"), "Post title");
        soft.assertNotNull(response.jsonPath().getString("body"), "Post body");
        soft.assertAll();
    }

    @Test(groups = {ApiConstants.GROUP_SANITY},
            description = "POST /posts creates a new post and returns HTTP 201")
    public void createPostReturns201() {
        log.info("Creating a new post");

        Map<String, Object> body = new HashMap<>();
        body.put("title", "Test post from Java");
        body.put("body", "Created by JavaApiTest");
        body.put("userId", 1);

        Response response = RequestHelper.post(ApiConstants.POSTS_ENDPOINT, body);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_CREATED, "Status code");
        soft.assertNotNull(response.jsonPath().getString("id"), "Created post id");
        soft.assertEquals(response.jsonPath().getString("title"), "Test post from Java", "Title");
        soft.assertAll();

        log.info("Created post with id={}", response.jsonPath().getString("id"));
    }

    @Test(groups = {ApiConstants.GROUP_SANITY},
            description = "GET /posts?userId=1 filters posts by userId correctly")
    public void getPostsFilteredByUserIdReturnsOnlyMatchingPosts() {
        log.info("Fetching posts for userId=1");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);

        Response response = RequestHelper.get(ApiConstants.POSTS_ENDPOINT, params);

        List<Integer> userIds = response.jsonPath().getList("userId");

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, "Status code");
        soft.assertTrue(userIds.stream().allMatch(id -> id == 1),
                "All returned posts belong to userId=1");
        soft.assertAll();

        log.info("Received {} posts for userId=1", userIds.size());
    }

    @Test(groups = {ApiConstants.GROUP_REGRESSION},
            description = "GET /posts/9999 returns HTTP 404 for a non-existent post")
    public void getNonExistentPostReturns404() {
        log.info("Fetching non-existent post id=9999");

        Response response = RequestHelper.get(ApiConstants.POSTS_ENDPOINT + "/9999");

        response.then()
                .statusCode(ApiConstants.HTTP_NOT_FOUND);

        log.info("Received expected 404 for non-existent post");
    }
}