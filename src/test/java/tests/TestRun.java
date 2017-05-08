package tests;

import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/feature",
        glue = {"stepdefinition"},
        format = {"pretty", "html:target/cucumber"},
        tags = {"@wip"}
)
public class TestRun {

}
