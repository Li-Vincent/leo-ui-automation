package automation.ui.datafactory;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import automation.datas.excelmodel.SampleDataModel;
import automation.ui.scripts.Jira1Test;

public class JIRA_1 {
    @DataProvider(name = "jira1")
    public static Object[][] data1() {
        Map<String, SampleDataModel> datas = SampleDataModel.read("datas/testSample.xlsx");
        return new Object[][]{
                // @formatter:off
                {"JIRA_1", "default1", datas.get("JIRA_1" + "default1")},
                {"JIRA_1", "default2", datas.get("JIRA_1" + "default2")},
                {"JIRA_1", "default3", datas.get("JIRA_1" + "default3")},
                {"JIRA_1", "default4", datas.get("JIRA_1" + "default4")},
                // @formatter:off
        };
    }

    @Factory(dataProvider = "jira1")
    public Object[] createTest(String testName, String scenario, SampleDataModel data) {

        Object[] tests = new Object[1];

        tests[0] = new Jira1Test(testName, scenario, data);

        return tests;
    }
}
