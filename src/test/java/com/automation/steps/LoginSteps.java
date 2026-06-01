package com.automation.steps;

import com.automation.pages.LoginPage;
import com.automation.pages.ProductsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
//import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;

/**
 * LoginSteps – Cucumber step definitions for login scenarios.
 *
 * Step definition classes are instantiated per-scenario by Cucumber.
 * Serenity @Steps injects action objects with screenshot/reporting wiring.
 */
public class LoginSteps {

    // Serenity injects page objects with full proxy/reporting support
    private LoginPage loginPage;
    private ProductsPage productsPage;

    // ─── Given ────────────────────────────────────────────────────────────────────

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage.openLoginPage();
    }

    // ─── When ─────────────────────────────────────────────────────────────────────

    @When("the user enters username {string} and password {string}")
    public void theUserEntersCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("the user clicks the login button")
    public void theUserClicksLoginButton() {
        loginPage.clickLogin();
    }

    // ─── Then ─────────────────────────────────────────────────────────────────────

    @Then("the user should be redirected to the products page")
    public void theUserShouldBeOnProductsPage() {
        Assertions.assertThat(productsPage.isOnProductsPage())
                  .as("Expected to be on the products/inventory page")
                  .isTrue();
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        Assertions.assertThat(productsPage.getProductsPageTitle())
                  .as("Products page title")
                  .isEqualTo(expectedTitle);
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        Assertions.assertThat(loginPage.isErrorMessageDisplayed())
                  .as("Error message should be visible")
                  .isTrue();
    }

    @And("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedText) {
        Assertions.assertThat(loginPage.getErrorMessageText())
                  .as("Error message text")
                  .contains(expectedText);
    }
}
