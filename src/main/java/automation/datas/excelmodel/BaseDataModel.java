package automation.datas.excelmodel;

import com.alibaba.excel.annotation.ExcelProperty;

public class BaseDataModel {
    @ExcelProperty(value = "TestName", index = 0)
    private String testName;

    @ExcelProperty(value = "Scenario", index = 1)
    private String scenario;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
}
