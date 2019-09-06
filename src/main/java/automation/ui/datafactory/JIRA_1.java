package automation.ui.datafactory;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import automation.ui.scripts.Jira1Test;

public class JIRA_1 {
    @DataProvider(name = "jira1")
    public static Object[][] data1() {
        return new Object[][] {
            // @formatter:off
            { "default1","pass" },
            { "default2","skip" },
            { "default3","fail" },
            { "default4","exception" }
            // @formatter:off
        };
    }

    @Factory(dataProvider = "jira1")
    public Object[] createTest(String scenario,String t) {

        Object[] tests = new Object[1];

        tests[0] = new Jira1Test(scenario,t);

        return tests;
    }
}
