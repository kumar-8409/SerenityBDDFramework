package com.automation.utils;

import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil – manual screenshot capture utility.
 *
 * Note: Serenity captures screenshots automatically per serenity.conf settings.
 * Use this for manual captures at custom points in your tests.
 */
public class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    /**
     * Captures a screenshot and saves to target/screenshots/<timestamp>_<name>.png
     *
     * @param name descriptive name for the screenshot
     * @return path to the saved file, or null on failure
     */
    public static Path capture(String name) {
        WebDriver driver = ThucydidesWebDriverSupport.getDriver();
        if (driver == null) {
            log.warn("No active WebDriver – skipping screenshot");
            return null;
        }

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String safeName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
            Path destination = Paths.get(SCREENSHOT_DIR, timestamp + "_" + safeName + ".png");

            Files.createDirectories(destination.getParent());
            Files.copy(srcFile.toPath(), destination);
            log.info("Screenshot saved: {}", destination);
            return destination;

        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns raw screenshot bytes for embedding in Cucumber reports.
     */
    public static byte[] captureAsBytes() {
        WebDriver driver = ThucydidesWebDriverSupport.getDriver();
        if (driver == null) return new byte[0];
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
