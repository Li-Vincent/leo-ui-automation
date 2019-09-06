package automation.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DemoPage extends BasePage {

    public DemoPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.ID, using = "agree")
    public WebElement termsElement;
}
