package com.automation.steps;

import com.automation.pages.CartPage;
import com.automation.pages.LoginPage;
import com.automation.pages.ProductsPage;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

/**
 * CartSteps – step definitions for shopping cart and checkout scenarios.
 */
public class CartSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    // ─── Given ────────────────────────────────────────────────────────────────────

    @Given("the user is logged in as {string}")
    public void theUserIsLoggedInAs(String username) {
        loginPage.openLoginPage();
        loginPage.loginAs(username, "secret_sauce");
        Assertions.assertThat(productsPage.isOnProductsPage())
                  .as("Should be on products page after login")
                  .isTrue();
    }

    @Given("the user has added {string} to the cart")
    public void theUserHasAddedProductToCart(String productName) {
        productsPage.addProductToCart(productName);
    }

    // ─── When ─────────────────────────────────────────────────────────────────────

    @When("the user adds {string} to the cart")
    public void theUserAddsProductToCart(String productName) {
        productsPage.addProductToCart(productName);
    }

    @When("the user adds the following products to the cart:")
    public void theUserAddsMultipleProductsToCart(List<String> products) {
        products.forEach(productsPage::addProductToCart);
    }

    @When("the user removes {string} from the cart")
    public void theUserRemovesProductFromCart(String productName) {
        productsPage.removeProductFromCart(productName);
    }

    @When("the user proceeds to checkout")
    public void theUserProceedsToCheckout() {
        productsPage.goToCart();
        cartPage.clickCheckout();
    }

    @When("the user enters shipping details:")
    public void theUserEntersShippingDetails(Map<String, String> details) {
        cartPage.enterShippingDetails(
                details.get("firstName"),
                details.get("lastName"),
                details.get("zipCode")
        );
    }

    @When("the user completes the order")
    public void theUserCompletesTheOrder() {
        cartPage.clickFinish();
    }

    // ─── Then ─────────────────────────────────────────────────────────────────────

    @Then("the cart badge count should be {string}")
    public void theCartBadgeCountShouldBe(String expectedCount) {
        Assertions.assertThat(productsPage.getCartBadgeCount())
                  .as("Cart badge count")
                  .isEqualTo(expectedCount);
    }

    @Then("the product {string} should appear in the cart")
    public void theProductShouldAppearInCart(String productName) {
        productsPage.goToCart();
        Assertions.assertThat(cartPage.isProductInCart(productName))
                  .as("Product '" + productName + "' should be in cart")
                  .isTrue();
    }

    @Then("the cart badge should not be visible")
    public void theCartBadgeShouldNotBeVisible() {
        Assertions.assertThat(productsPage.isCartBadgeVisible())
                  .as("Cart badge should not be visible")
                  .isFalse();
    }

    @Then("the order confirmation should be displayed")
    public void theOrderConfirmationShouldBeDisplayed() {
        Assertions.assertThat(cartPage.isOrderConfirmationDisplayed())
                  .as("Order confirmation header should be displayed")
                  .isTrue();
    }

    @And("the confirmation message should contain {string}")
    public void theConfirmationMessageShouldContain(String expectedText) {
        Assertions.assertThat(cartPage.getConfirmationMessage())
                  .as("Confirmation message")
                  .contains(expectedText);
    }
}
