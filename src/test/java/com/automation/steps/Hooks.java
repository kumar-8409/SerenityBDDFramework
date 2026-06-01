package com.automation.steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hooks – Cucumber lifecycle hooks for setup and teardown.
 *
 * Serenity manages WebDriver lifecycle automatically; these hooks
 * are for business-level setup and custom screenshot logic.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    /**
     * Runs BEFORE every scenario.
     * Order = 0 means it runs first if multiple @Before hooks exist.
     */
    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("▶  Starting scenario: {}", scenario.getName());
        log.info("   Tags: {}", scenario.getSourceTagNames());
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Runs AFTER every scenario.
     * Takes screenshot on failure (Serenity also does this automatically).
     */
    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("✗ Scenario FAILED: {}", scenario.getName());
            // Serenity automatically captures screenshots on failure via serenity.conf
            // Manual screenshot can be embedded into Cucumber report:
            try {
                byte[] screenshot = ThucydidesWebDriverSupport.getDriver()
                        .manage().window() != null
                        ? ((org.openqa.selenium.TakesScreenshot)
                           ThucydidesWebDriverSupport.getDriver())
                           .getScreenshotAs(org.openqa.selenium.OutputType.BYTES)
                        : null;
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");
                }
            } catch (Exception e) {
                log.warn("Could not capture screenshot: {}", e.getMessage());
            }
        } else {
            log.info("✓ Scenario PASSED: {}", scenario.getName());
        }
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }

    /**
     * Runs AFTER every step – useful for step-level screenshots or logging.
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        // Serenity handles step screenshots via serenity.conf take.screenshots setting.
        // Add custom post-step logic here if needed.
    }
}
