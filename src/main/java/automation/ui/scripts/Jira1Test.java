package automation.ui.scripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import automation.ui.base.BaseTest;
import automation.ui.base.Result;
import automation.ui.common.UIController;

public class Jira1Test extends BaseTest {

    public Jira1Test(String scenario) {
        super("JIRA_1", scenario);
    }

    @Test(enabled = true, alwaysRun = true)
    public void JIRA_1(ITestContext context) {
        allTestStep(context);
    }

    @Override
    protected Result onPreCondition() {
        prepareTest();
        return Result.PASS;
    }

    @Override
    protected Result onTest() {
        driver.get("https://ssl.zc.qq.com/v3/index-chs.html");

        WebElement terms = driver.findElement(By.id("agree"));
        WebElement submit = driver.findElement(By.id("get_acc" + "testerror"));
        UIController.click(driver, submit);
        System.out.println(terms.isSelected());
        UIController.tick(driver, terms, false);

        WebElement firstElement = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[1]/div[1]/div[1]"));
        System.out.println(firstElement.getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement)).getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement, "div", "div", 2)).getText());
        return Result.PASS;
    }

    @Override
    protected Result onPostCondition() {
        return Result.PASS;
    }
}
