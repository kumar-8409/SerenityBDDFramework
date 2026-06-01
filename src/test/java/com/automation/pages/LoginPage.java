package com.automation.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
//import net.thucydides.core.annotations.DefaultUrl;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

/**
 * LoginPage – Page Object for https://www.saucedemo.com
 *
 * Serenity's @FindBy is used instead of Selenium's so that
 * elements are automatically waited for and wrapped with
 * screenshot + reporting hooks.
 */
@DefaultUrl("https://www.saucedemo.com")
public class LoginPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────────
    private static final By USERNAME_FIELD   = By.id("user-name");
    private static final By PASSWORD_FIELD   = By.id("password");
    private static final By LOGIN_BUTTON     = By.id("login-button");
    private static final By ERROR_MESSAGE    = By.cssSelector("[data-test='error']");

    // Serenity WebElementFacade – richer API than plain WebElement
    @FindBy(id = "user-name")
    private WebElementFacade usernameField;

    @FindBy(id = "password")
    private WebElementFacade passwordField;

    @FindBy(id = "login-button")
    private WebElementFacade loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElementFacade errorMessage;

    // ─── Page Actions ─────────────────────────────────────────────────────────────

    public void openLoginPage() {
        log.info("Opening login page");
        open();  // navigates to @DefaultUrl
    }

    public void enterUsername(String username) {
        log.info("Entering username: {}", username);
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        log.info("Entering password");
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        log.info("Clicking login button");
        loginButton.click();
    }

    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ─── Assertions / Queries ─────────────────────────────────────────────────────

    public boolean isErrorMessageDisplayed() {
        return errorMessage.isCurrentlyVisible();
    }

    public String getErrorMessageText() {
        return errorMessage.getText();
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("saucedemo.com") && loginButton.isCurrentlyVisible();
    }
}
