package com.automation.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

//import net.thucydides.core.util.SystemEnvironmentVariables;
//import net.thucydides.core.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FrameworkConfig – centralised configuration helper.
 *
 * Reads from:
 *   1. serenity.conf  (default)
 *   2. System properties / env vars  (-Dbrowser=firefox)
 *
 * Usage in steps:
 *   String baseUrl = FrameworkConfig.getBaseUrl();
 */
public class FrameworkConfig {

    private static final Logger log = LoggerFactory.getLogger(FrameworkConfig.class);
    private static final EnvironmentVariables envVars =
            SystemEnvironmentVariables.createEnvironmentVariables();

    // ─── Driver setup ──────────────────────────────────────────────────────────

    public static void setupDriver(String browser) {
        log.info("Setting up WebDriver for browser: {}", browser);
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                break;
        }
    }

    // ─── Config getters ────────────────────────────────────────────────────────

    public static String getBrowser() {
        return System.getProperty("browser",
               envVars.getProperty("webdriver.driver", "chrome"));
    }

    public static String getBaseUrl() {
        return envVars.getProperty("webdriver.base.url", "https://www.saucedemo.com");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    public static int getDefaultTimeout() {
        return Integer.parseInt(
                envVars.getProperty("serenity.webdriver.wait.for.timeout", "10000")) / 1000;
    }

    public static String getEnvironment() {
        return System.getProperty("environment", "default");
    }
}
