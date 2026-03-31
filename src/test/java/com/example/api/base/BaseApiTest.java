package com.example.api.base;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all API tests.
 * Sets up global REST Assured configuration once per suite.
 * Subclasses must not declare mutable state as fields.
 */
public abstract class BaseApiTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        log.info("REST Assured configured — base URI: {}", ApiConstants.BASE_URL);
    }
}