package com.example.api.tests

import com.example.api.base.ApiConstants
import com.example.api.base.BaseApiTest
import com.example.api.base.RequestHelper
import org.testng.Assert
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert

/**
 * API tests written in Kotlin.
 * Demonstrates idiomatic Kotlin: concise syntax, data classes, named arguments,
 * string templates, and lambda expressions.
 * All mutable state is kept in local variables — safe for parallel execution.
 */
class KotlinApiTest : BaseApiTest() {

    @Test(
        groups = [ApiConstants.GROUP_SMOKE],
        description = "GET /users returns HTTP 200 and exactly 10 users"
    )
    fun getAllUsersReturnsTenUsers() {
        log.info("Fetching all users")

        val response = RequestHelper.get(ApiConstants.USERS_ENDPOINT)
        val users: List<*> = response.jsonPath().getList<Any>("$")

        val soft = SoftAssert()
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, "Status code")
        soft.assertEquals(users.size, 10, "Expected exactly 10 users")
        soft.assertAll()

        log.info("Received ${users.size} users")
    }

    @Test(
        groups = [ApiConstants.GROUP_SMOKE],
        description = "GET /users/1 returns user with correct email"
    )
    fun getUserByIdHasValidEmail() {
        log.info("Fetching user id=1")

        val response = RequestHelper.get("${ApiConstants.USERS_ENDPOINT}/1")

        val soft = SoftAssert()
        soft.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, "Status code")

        val email: String = response.jsonPath().getString("email")
        soft.assertNotNull(email, "Email should not be null")
        soft.assertTrue(email.contains("@"), "Email should contain '@'")
        soft.assertAll()

        log.info("User email: $email")
    }

    @Test(
        groups = [ApiConstants.GROUP_SANITY],
        description = "GET /todos returns items with boolean completed field"
    )
    fun getTodosHaveBooleanCompletedField() {
        log.info("Fetching todos")

        val response = RequestHelper.get(ApiConstants.TODOS_ENDPOINT)
        val completedValues: List<Boolean> = response.jsonPath().getList("completed")

        Assert.assertEquals(response.statusCode(), ApiConstants.HTTP_OK, "Status code")
        Assert.assertFalse(completedValues.isEmpty(), "Todos list should not be empty")

        // Kotlin: concise check that every value is genuinely Boolean (not null)
        val allAreBoolean = completedValues.all { it != null }
        Assert.assertTrue(allAreBoolean, "Every 'completed' field should be a boolean")

        log.info("Found ${completedValues.size} todos, ${completedValues.count { it }} completed")
    }
}