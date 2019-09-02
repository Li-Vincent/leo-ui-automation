package automation.ui.scripts;

import java.io.InputStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import automation.ui.base.BaseTest;
import automation.ui.common.ScreenShot;
import automation.ui.common.UIController;

public class Jira1Test extends BaseTest {

    public Jira1Test(String scenario) {
        super("JIRA_1");
        this.scenario = scenario;
    }

    @Test(enabled = true, alwaysRun = true)
    public void JIRA_1() {
        this.prepareTest();
        driver.get("https://ssl.zc.qq.com/v3/index-chs.html");

        WebElement terms = driver.findElement(By.id("agree"));
        WebElement submit = driver.findElement(By.id("get_acc"));
        UIController.click(driver, submit);
        System.out.println(terms.isSelected());
        UIController.tick(driver, terms, false);

        WebElement firstElement = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[1]/div[1]/div[1]"));
        System.out.println(firstElement.getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement)).getText());
        System.out.println(((WebElement) UIController.getNextElement(driver, firstElement, "div", "div", 2)).getText());
    }
}
