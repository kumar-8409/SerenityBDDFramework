package com.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * WaitHelper – custom wait strategies not covered by Serenity's built-in waits.
 */
public class WaitHelper {

    private static final Logger log = LoggerFactory.getLogger(WaitHelper.class);
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int POLLING_INTERVAL = 500;

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    // ─── Standard waits ────────────────────────────────────────────────────────

    public WebElement waitForVisible(By locator) {
        log.debug("Waiting for visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        log.debug("Waiting for clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisible(By locator) {
        log.debug("Waiting for invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForUrl(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    // ─── Fluent wait (custom polling + ignore exceptions) ─────────────────────

    public WebElement fluentWait(By locator, int timeoutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .until(d -> d.findElement(locator));
    }

    // ─── JavaScript waits ─────────────────────────────────────────────────────

    public void waitForPageLoad() {
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        log.debug("Page fully loaded");
    }

    public void waitForAjax() {
        wait.until(d -> {
            Boolean jQueryDone = (Boolean)
                    ((JavascriptExecutor) d).executeScript(
                            "return (typeof jQuery === 'undefined') || jQuery.active === 0");
            return jQueryDone;
        });
    }

    // ─── Thread sleep (last resort) ───────────────────────────────────────────

    public static void sleepMillis(long ms) {
        try {
            log.warn("Hard sleep {}ms – consider using explicit waits", ms);
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
