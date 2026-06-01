package com.automation.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * TestRunner – main entry point for the test suite.
 *
 * Run all tests:       mvn verify
 * Run by tag:          mvn verify -Dcucumber.filter.tags="@smoke"
 * Run headless:        mvn verify -Dheadless=true
 * Switch environment:  mvn verify -Denvironment=staging
 */
@RunWith(CucumberWithSerenity.class)   // ← Serenity's runner (NOT standard Cucumber)
@CucumberOptions(
        // Path to .feature files
        features = "src/test/resources/features",

        // Path to step definition classes
        glue = {
                "com.automation.steps",
                "com.automation.config"
        },

        // Plugins for reporting
        plugin = {
                "pretty",                                          // console output
                "json:target/cucumber-reports/cucumber.json",     // JSON for CI tools
                "html:target/cucumber-reports/cucumber.html",     // basic HTML
                "rerun:target/cucumber-reports/rerun.txt"         // failed scenarios list
        },

        // Show monochrome output in CI environments
        monochrome = true,

        // Tags to run (overridden at runtime via -Dtags or -Dcucumber.filter.tags)
        tags = "not @wip"
)
public class TestRunner {
    // Intentionally empty – configuration is via annotations above
}
