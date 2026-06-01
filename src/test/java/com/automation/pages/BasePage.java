package com.automation.pages;

import net.serenitybdd.core.pages.PageObject;
//import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * BasePage – parent of all Page Objects.
 *
 * Extends Serenity's {@link PageObject} which integrates with WebDriver,
 * screenshots, and the Serenity reporting engine automatically.
 */
public abstract class BasePage extends PageObject {

    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);
    private static final int DEFAULT_WAIT_SECONDS = 10;

    // ─── Reusable wait helpers ──────────────────────────────────────────────────

    protected WebElement waitForElement(By locator) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT_SECONDS))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean isElementPresent(By locator) {
        try {
            return getDriver().findElements(locator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    protected List<WebElement> findAllElements(By locator) {
        return getDriver().findElements(locator);
    }

    // ─── Common actions ─────────────────────────────────────────────────────────

    protected void clickOn(By locator) {
        log.debug("Clicking element: {}", locator);
        waitForClickable(locator).click();
    }

    protected void typeInto(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getTextOf(By locator) {
        return waitForElement(locator).getText();
    }

    // ─── Page utilities ─────────────────────────────────────────────────────────

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
}
