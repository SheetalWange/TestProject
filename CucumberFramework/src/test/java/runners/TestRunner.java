package runners;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/CucumberFramework/featureFiles",
		glue = "CucumberFramework/stepFiles",
		tags = {"@FrontendTest,@BackendTest"})
public class TestRunner {
}