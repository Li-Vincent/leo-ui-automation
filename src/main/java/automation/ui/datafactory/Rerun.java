package automation.ui.datafactory;

import automation.ui.scripts.RerunTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class Rerun {
    @DataProvider(name = "rerun")
    public static Object[][] data1() {
        return new Object[][]{
                // @formatter:off
                {"default1"},
                {"default2"},
                {"default3"}
                // @formatter:off
        };
    }

    @Factory(dataProvider = "rerun")
    public Object[] createTest(String scenario) {

        Object[] tests = new Object[1];

        tests[0] = new RerunTest(scenario);

        return tests;
    }
}