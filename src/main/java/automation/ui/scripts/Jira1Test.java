package automation.ui.scripts;

import automation.datas.excelmodel.SampleDataModel;
import automation.ui.base.BaseTest;
import automation.ui.base.Result;
import automation.ui.common.UIController;
import automation.ui.pages.DemoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class Jira1Test extends BaseTest {
    String name = "";

    public Jira1Test(String testName, String scenario, SampleDataModel data) {
        super(testName, scenario);
        name = data.getName();
    }

    @Test(enabled = true, alwaysRun = true)
    public void JIRA_1(ITestContext context) throws Exception {
        allTestStep(context);
    }

    @Override
    protected Result onPreCondition() {
        prepareTest();
        if (name.equals("name2")) {
            return Result.SKIP;
        }
        return Result.PASS;
    }

    @Override
    protected Result onTest() {
        driver.get("https://ssl.zc.qq.com/v3/index-chs.html");

        DemoPage demoPage = new DemoPage(driver);
        WebElement terms = demoPage.termsElement;
        WebElement submit = null;
        if (name.equals("name3")) {
            submit = driver.findElement(By.id("get_acc" + name));
        } else {
            submit = driver.findElement(By.id("get_acc"));
        }
        UIController.click(driver, submit);
        System.out.println(terms.isSelected());
        UIController.tick(driver, terms, false);

        WebElement firstElement = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[1]/div[1]/div[1]"));
        System.out.println(firstElement.getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement)).getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement, "div", "div", 2)).getText());
        if (name.equals("name4")) {
            return Result.FAIL;
        }
        return Result.PASS;
    }

    @Override
    protected Result onPostCondition() {
        return Result.PASS;
    }
}
