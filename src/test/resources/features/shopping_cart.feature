# ============================================================
# Feature: Shopping Cart
# ============================================================
@cart @regression
Feature: Shopping Cart Management

  Background:
    Given the user is logged in as "standard_user"

  @positive
  Scenario: Add a product to the shopping cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge count should be "1"
    And the product "Sauce Labs Backpack" should appear in the cart

  @positive
  Scenario: Add multiple products to the cart
    When the user adds the following products to the cart:
      | Sauce Labs Backpack    |
      | Sauce Labs Bike Light  |
    Then the cart badge count should be "2"

  @positive
  Scenario: Remove a product from the cart
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user removes "Sauce Labs Backpack" from the cart
    Then the cart badge should not be visible

  @positive
  Scenario: Complete checkout flow
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user proceeds to checkout
    And the user enters shipping details:
      | firstName | John        |
      | lastName  | Doe         |
      | zipCode   | 12345       |
    And the user completes the order
    Then the order confirmation should be displayed
    And the confirmation message should contain "Thank you for your order"
