package com.automation.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductsPage – the main inventory/products listing page.
 */
public class ProductsPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────────
    private static final By PAGE_TITLE         = By.cssSelector(".title");
    private static final By CART_BADGE         = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK          = By.cssSelector(".shopping_cart_link");
    private static final By INVENTORY_ITEMS    = By.cssSelector(".inventory_item");
    private static final By PRODUCT_NAME       = By.cssSelector(".inventory_item_name");

    @FindBy(css = ".title")
    private WebElementFacade pageTitle;

    @FindBy(css = ".shopping_cart_badge")
    private WebElementFacade cartBadge;

    // ─── Page Actions ─────────────────────────────────────────────────────────────

    public boolean isOnProductsPage() {
        return getCurrentUrl().contains("inventory") && pageTitle.isCurrentlyVisible();
    }

    public String getProductsPageTitle() {
        return getTextOf(PAGE_TITLE);
    }

    /**
     * Adds a product to the cart by matching the product name on the page.
     */
    public void addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        List<WebElement> items = findAllElements(INVENTORY_ITEMS);

        items.stream()
             .filter(item -> item.findElement(By.cssSelector(".inventory_item_name"))
                                 .getText().equalsIgnoreCase(productName))
             .findFirst()
             .ifPresent(item -> {
                 WebElement addBtn = item.findElement(By.cssSelector("button[id^='add-to-cart']"));
                 addBtn.click();
                 log.info("Clicked Add to Cart for: {}", productName);
             });
    }

    public void removeProductFromCart(String productName) {
        log.info("Removing product from cart: {}", productName);
        List<WebElement> items = findAllElements(INVENTORY_ITEMS);

        items.stream()
             .filter(item -> item.findElement(By.cssSelector(".inventory_item_name"))
                                 .getText().equalsIgnoreCase(productName))
             .findFirst()
             .ifPresent(item -> {
                 WebElement removeBtn = item.findElement(By.cssSelector("button[id^='remove']"));
                 removeBtn.click();
             });
    }

    public String getCartBadgeCount() {
        return isElementPresent(CART_BADGE) ? getTextOf(CART_BADGE) : "0";
    }

    public boolean isCartBadgeVisible() {
        return isElementPresent(CART_BADGE);
    }

    public void goToCart() {
        clickOn(CART_LINK);
    }

    public List<String> getAllProductNames() {
        return findAllElements(PRODUCT_NAME)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
