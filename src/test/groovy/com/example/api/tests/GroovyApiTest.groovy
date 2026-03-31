package com.example.api.tests

import com.example.api.base.ApiConstants
import com.example.api.base.BaseApiTest
import com.example.api.base.RequestHelper
import io.restassured.response.Response
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert

/**
 * API tests written in Groovy.
 * Demonstrates idiomatic Groovy: GStrings, dynamic typing, closures,
 * and concise collection operations.
 * All mutable state is kept in local variables — safe for parallel execution.
 */
class GroovyApiTest extends BaseApiTest {

    @Test(groups = [ApiConstants.GROUP_SMOKE],
          description = 'GET /comments returns HTTP 200 and items with postId field')
    void getAllCommentsHavePostIdField() {
        log.info('Fetching all comments')

        Response response = RequestHelper.get(ApiConstants.COMMENTS_ENDPOINT)
        def comments = response.jsonPath().getList('$')

        SoftAssert soft = new SoftAssert()
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, 'Status code')
        soft.assertFalse(comments.isEmpty(), 'Comments list should not be empty')

        // Groovy closure: every comment should have a postId
        boolean allHavePostId = comments.every { it.postId != null }
        soft.assertTrue(allHavePostId, 'Every comment should have a postId')
        soft.assertAll()

        log.info("Received ${comments.size()} comments")
    }

    @Test(groups = [ApiConstants.GROUP_SANITY],
          description = 'GET /comments?postId=1 returns comments only for postId=1')
    void getCommentsFilteredByPostIdReturnOnlyMatchingComments() {
        log.info('Fetching comments for postId=1')

        Response response = RequestHelper.get(ApiConstants.COMMENTS_ENDPOINT, [postId: 1])
        def postIds = response.jsonPath().getList('postId')

        SoftAssert soft = new SoftAssert()
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, 'Status code')
        soft.assertFalse(postIds.isEmpty(), 'Should return at least one comment')

        // Groovy: concise every{} closure check
        soft.assertTrue(postIds.every { it == 1 }, 'All comments should belong to postId=1')
        soft.assertAll()

        log.info("Found ${postIds.size()} comments for postId=1")
    }

    @Test(groups = [ApiConstants.GROUP_REGRESSION],
          description = 'GET /users/1 contains an address with a geo block')
    void getUserAddressContainsGeoCoordinates() {
        log.info('Fetching user id=1 to verify nested address.geo')

        Response response = RequestHelper.get("${ApiConstants.USERS_ENDPOINT}/1")

        SoftAssert soft = new SoftAssert()
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, 'Status code')

        // Groovy GPath — concise deep property access
        def lat = response.jsonPath().getString('address.geo.lat')
        def lng = response.jsonPath().getString('address.geo.lng')

        soft.assertNotNull(lat, 'geo.lat should not be null')
        soft.assertNotNull(lng, 'geo.lng should not be null')
        soft.assertAll()

        log.info("User geo: lat=${lat}, lng=${lng}")
    }
}