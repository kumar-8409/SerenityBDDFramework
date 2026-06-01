package com.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TestDataHelper – centralised test data store.
 *
 * Stores common user credentials, product names, and shared state
 * across steps within a single scenario via a ThreadLocal map.
 */
public class TestDataHelper {

    private static final Logger log = LoggerFactory.getLogger(TestDataHelper.class);

    // ThreadLocal ensures data isolation between parallel test threads
    private static final ThreadLocal<Map<String, String>> scenarioContext =
            ThreadLocal.withInitial(HashMap::new);

    // ─── Pre-defined test users (from SauceDemo) ───────────────────────────────

    public static final String STANDARD_USER  = "standard_user";
    public static final String LOCKED_USER    = "locked_out_user";
    public static final String PROBLEM_USER   = "problem_user";
    public static final String PERF_USER      = "performance_glitch_user";
    public static final String PASSWORD       = "secret_sauce";

    // ─── Pre-defined products ──────────────────────────────────────────────────

    public static final String BACKPACK       = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT     = "Sauce Labs Bike Light";
    public static final String BOLT_TSHIRT    = "Sauce Labs Bolt T-Shirt";
    public static final String FLEECE_JACKET  = "Sauce Labs Fleece Jacket";

    // ─── Scenario context (key-value store per scenario) ──────────────────────

    public static void set(String key, String value) {
        log.debug("Setting context: {} = {}", key, value);
        scenarioContext.get().put(key, value);
    }

    public static String get(String key) {
        return scenarioContext.get().get(key);
    }

    public static void clear() {
        log.debug("Clearing scenario context");
        scenarioContext.get().clear();
    }

    // ─── Sample checkout data factory ─────────────────────────────────────────

    public static Map<String, String> getValidCheckoutData() {
        Map<String, String> data = new HashMap<>();
        data.put("firstName", "John");
        data.put("lastName", "Doe");
        data.put("zipCode", "12345");
        return data;
    }
}
