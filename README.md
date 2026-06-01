# 🧪 Serenity BDD Framework
### Cucumber · Java · Selenium · Maven

A production-ready end-to-end test automation framework using **Serenity BDD**, **Cucumber 7**, **Selenium 4**, and **JUnit 4**.

---

## 📂 Project Structure

```
serenity-bdd-framework/
├── pom.xml                                          # Maven dependencies & plugins
└── src/
    └── test/
        ├── java/com/automation/
        │   ├── config/
        │   │   └── FrameworkConfig.java             # Env/browser config helper
        │   ├── pages/
        │   │   ├── BasePage.java                    # Parent Page Object (extends PageObject)
        │   │   ├── LoginPage.java                   # Login page actions & locators
        │   │   ├── ProductsPage.java                # Products/inventory page
        │   │   └── CartPage.java                    # Cart & checkout pages
        │   ├── steps/
        │   │   ├── LoginSteps.java                  # Step defs for login scenarios
        │   │   ├── CartSteps.java                   # Step defs for cart/checkout
        │   │   └── Hooks.java                       # @Before / @After hooks
        │   ├── runners/
        │   │   ├── TestRunner.java                  # Main Cucumber runner
        │   │   └── RerunFailedRunner.java           # Re-run failed tests
        │   └── utils/
        │       ├── TestDataHelper.java              # Test data & scenario context
        │       ├── WaitHelper.java                  # Custom wait strategies
        │       └── ScreenshotUtil.java              # Manual screenshot capture
        └── resources/
            ├── serenity.conf                        # Serenity configuration
            ├── logback-test.xml                     # Logging config
            └── features/
                ├── login.feature                    # Login scenarios
                └── shopping_cart.feature            # Cart & checkout scenarios
```

---

## ⚙️ Prerequisites

| Tool        | Version    |
|-------------|------------|
| Java JDK    | 11+        |
| Maven       | 3.8+       |
| Chrome      | Latest     |
| Git         | Any        |

---

## 🚀 Running Tests

### Run all tests
```bash
mvn clean verify
```

### Run by tag
```bash
mvn clean verify -Dcucumber.filter.tags="@smoke"
mvn clean verify -Dcucumber.filter.tags="@login and @positive"
mvn clean verify -Dcucumber.filter.tags="@regression and not @wip"
```

### Run in headless mode
```bash
mvn clean verify -Dheadless=true
```

### Run against a specific environment
```bash
mvn clean verify -Denvironment=staging
```

### Run in a different browser
```bash
mvn clean verify -Dbrowser=firefox
mvn clean verify -Dbrowser=edge
```

### Re-run only failed scenarios
```bash
# Step 1 – Run main suite (generates rerun.txt)
mvn clean verify

# Step 2 – Run failures only
mvn verify -Dtest=RerunFailedRunner
```

---

## 📊 Reports

Serenity generates rich HTML reports automatically after every run.

| Report Type       | Location                              |
|-------------------|---------------------------------------|
| Serenity HTML     | `target/site/serenity/index.html`     |
| Cucumber JSON     | `target/cucumber-reports/cucumber.json` |
| Cucumber HTML     | `target/cucumber-reports/cucumber.html` |
| Failed rerun list | `target/cucumber-reports/rerun.txt`   |
| Logs              | `target/logs/test-execution.log`      |

```bash
# Open Serenity report after run:
open target/site/serenity/index.html      # macOS
start target/site/serenity/index.html     # Windows
xdg-open target/site/serenity/index.html  # Linux
```

---

## 🏗️ Framework Architecture

```
Feature Files  →  Step Definitions  →  Page Objects  →  WebDriver
     ↓                   ↓                  ↓               ↓
  Cucumber          Serenity Steps      BasePage        Selenium 4
  Scenarios         (inject pages)     (PageObject)    (Chrome/FF/Edge)
                          ↓
                   Serenity Reports
                   (HTML + JSON + Screenshots)
```

### Key Design Decisions

- **Page Object Model** – All UI interactions live in `pages/`, keeping steps thin.
- **Serenity PageObject** – Extends Serenity's `PageObject` for automatic screenshot, WebElementFacade, and reporting integration.
- **ThreadLocal context** – `TestDataHelper` uses `ThreadLocal<Map>` for safe parallel execution.
- **WebDriverManager** – Automatic driver binary management; no manual driver downloads.
- **Serenity.conf** – Centralised config with environment overrides via HOCON format.

---

## ➕ Adding New Tests

### 1. Write a Feature File
```gherkin
# src/test/resources/features/my_feature.feature
@my_tag
Feature: My New Feature
  Scenario: Do something useful
    Given some precondition
    When I perform an action
    Then I see the expected result
```

### 2. Create a Page Object
```java
// src/test/.../pages/MyPage.java
public class MyPage extends BasePage {
    private static final By MY_ELEMENT = By.id("my-element");

    public void clickMyElement() {
        clickOn(MY_ELEMENT);
    }
}
```

### 3. Write Step Definitions
```java
// src/test/.../steps/MySteps.java
public class MySteps {
    private MyPage myPage;  // Serenity auto-injects

    @When("I perform an action")
    public void iPerformAnAction() {
        myPage.clickMyElement();
    }
}
```

---

## 🏷️ Tag Strategy

| Tag           | Purpose                                  |
|---------------|------------------------------------------|
| `@smoke`      | Critical path – run on every PR          |
| `@regression` | Full regression – run nightly            |
| `@positive`   | Happy path scenarios                     |
| `@negative`   | Error/edge case scenarios                |
| `@login`      | Login feature group                      |
| `@cart`       | Shopping cart feature group              |
| `@wip`        | Work-in-progress – excluded from CI      |

---

## 🔧 CI/CD Integration (GitHub Actions example)

```yaml
# .github/workflows/tests.yml
name: Serenity BDD Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with: { java-version: '11', distribution: 'temurin' }
      - name: Run Smoke Tests
        run: mvn clean verify -Dheadless=true -Dcucumber.filter.tags="@smoke"
      - name: Upload Serenity Report
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: serenity-report
          path: target/site/serenity/
```
