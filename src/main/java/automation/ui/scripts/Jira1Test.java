package automation.ui.scripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import automation.ui.base.BaseTest;
import automation.ui.base.Result;
import automation.ui.common.UIController;

public class Jira1Test extends BaseTest {
    String testString = "";

    public Jira1Test(String scenario, String t) {
        super("JIRA_1", scenario);
        testString = t;
    }

    @Test(enabled = true, alwaysRun = true)
    public void JIRA_1(ITestContext context) throws Exception {
        allTestStep(context);
    }

    @Override
    protected Result onPreCondition() {
        prepareTest();
        if (testString == "skip") {
            return Result.SKIP;
        }
        return Result.PASS;
    }

    @Override
    protected Result onTest() {
        driver.get("https://ssl.zc.qq.com/v3/index-chs.html");

        WebElement terms = driver.findElement(By.id("agree"));
        WebElement submit = null;
        if (testString == "exception") {
            submit = driver.findElement(By.id("get_acc" + testString));
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
        if (testString == "fail") {
            return Result.FAIL;
        }
        return Result.PASS;
    }

    @Override
    protected Result onPostCondition() {
        return Result.PASS;
    }
}
