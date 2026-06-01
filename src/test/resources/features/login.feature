# ============================================================
# Feature: User Login
# Demonstrates Serenity BDD with Cucumber scenarios
# ============================================================
@login @smoke
Feature: User Authentication

  Background:
    Given the user is on the login page

  @positive
  Scenario: Successful login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be redirected to the products page
    And the page title should be "Products"

  @positive
  Scenario Outline: Login with multiple valid users
    When the user enters username "<username>" and password "<password>"
    And the user clicks the login button
    Then the user should be redirected to the products page

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |

  @negative
  Scenario: Login fails with invalid credentials
    When the user enters username "invalid_user" and password "wrong_pass"
    And the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username and password do not match"

  @negative
  Scenario: Login fails with empty credentials
    When the user clicks the login button
    Then an error message should be displayed
    And the error message should contain "Username is required"
