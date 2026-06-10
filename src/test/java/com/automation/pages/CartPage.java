package com.automation.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage – Shopping Cart and Checkout pages.
 */
public class CartPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────────
    private static final By CART_ITEMS        = By.cssSelector(".cart_item");
    private static final By CART_ITEM_NAME    = By.cssSelector(".inventory_item_name");
    private static final By CHECKOUT_BUTTON   = By.id("checkout");
    private static final By FIRST_NAME        = By.id("first-name");
    private static final By LAST_NAME         = By.id("last-name");
    private static final By ZIP_CODE          = By.id("postal-code");
    private static final By CONTINUE_BUTTON   = By.id("continue");
    private static final By FINISH_BUTTON     = By.id("finish");
    private static final By CONFIRM_HEADER    = By.cssSelector(".complete-header");
    private static final By CONFIRM_TEXT      = By.cssSelector(".complete-text");

    @FindBy(css = ".complete-header")
    private WebElementFacade confirmationHeader;

    // ─── Page Actions ─────────────────────────────────────────────────────────────

    public List<String> getCartItemNames() {
        return findAllElements(CART_ITEMS)
                .stream()
                .map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText())
                .collect(Collectors.toList());
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public void clickCheckout() {
        log.info("Clicking Checkout button");
        clickOn(CHECKOUT_BUTTON);
    }

    public void enterFirstName(String firstName) {
        typeInto(FIRST_NAME, firstName);
    }

    public void enterLastName(String lastName) {
        typeInto(LAST_NAME, lastName);
    }

    public void enterZipCode(String zipCode) {
        typeInto(ZIP_CODE, zipCode);
    }

    public void enterShippingDetails(String firstName, String lastName, String zipCode) {
        log.info("Entering shipping: {} {} {}", firstName, lastName, zipCode);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        clickOn(CONTINUE_BUTTON);
    }

    public void clickFinish() {
        log.info("Completing order");
        clickOn(FINISH_BUTTON);
    }

    public boolean isOrderConfirmationDisplayed() {
        return confirmationHeader.isCurrentlyVisible();
    }

    public String getConfirmationMessage() {
        return getTextOf(CONFIRM_TEXT);
    }
}
