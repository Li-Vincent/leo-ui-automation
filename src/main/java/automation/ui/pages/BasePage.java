package automation.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    public WebDriver PageDriver;

    public BasePage(WebDriver driver) {
        this.PageDriver = driver;
        PageFactory.initElements(PageDriver, this);
    }
}
