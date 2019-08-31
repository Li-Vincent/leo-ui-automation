package automation.ui.common;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.utils.ConfUtils;

public class UIController {

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception Ex) {
            return false;
        }
    }

    public static void acceptAlert(WebDriver driver) {
        if (isAlertPresent(driver)) {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
    }

    public static void cancelAlert(WebDriver driver) {
        if (isAlertPresent(driver)) {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        }
    }

    public static boolean tick(WebDriver driver, WebElement element) {
        return tick(driver, element, true);
    }

    public static boolean tick(WebDriver driver, WebElement element, boolean click) {
        if (click && element.isSelected()) {
            return true;
        }
        if (!click && !element.isSelected()) {
            return true;
        }
        return clickByJS(driver, element);
    }

    public static boolean click(WebDriver driver, WebElement element) {
        try {
            new WebDriverWait(driver, ConfUtils.getDefaultTimeout())
                    .until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean clickByJS(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click()", element);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void pageRefresh(WebDriver driver) {
        driver.navigate().refresh();
    }

    public static SearchContext getNextElement(WebDriver driver, SearchContext element) {
        return element.findElement(By.xpath("following-sibling::*"));
    }

    public static SearchContext getNextElement(WebDriver driver, SearchContext element, String tag, String nextTag,
            int count) {
        return element.findElement(By.xpath("following-sibling::" + nextTag + "[" + count + "]"));
    }

    public static SearchContext getPreviousElement(WebDriver driver, SearchContext element) {
        return element.findElement(By.xpath("preceding-sibling::*"));
    }

    public static SearchContext getPreviousElement(WebDriver driver, SearchContext element, String tag, String preTag,
            int count) {
        return element.findElement(By.xpath(tag + "/preceding-sibling::" + preTag + "[" + count + "]"));
    }

    public static void switchToWindow(WebDriver driver, int windowNo) {
        ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(windows.get(windowNo));
    }

    public static boolean switchToWindowByTitle(WebDriver driver, String windowTitle) {
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle))
                    continue;
                else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(windowTitle)) {
                        flag = true;
                        break;
                    } else
                        continue;
                }
            }
        } catch (NoSuchWindowException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * waitElementClickable
     * 
     * @author Vincent-Li
     */
    public static void waitElementClickable(WebDriver driver, WebElement element, int timeoutInSeconds)
            throws NoSuchElementException {
        new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * waitElementVisible
     * 
     * @author Vincent-Li
     */
    public static void waitElementVisible(WebDriver driver, WebElement element) throws InterruptedException {
        new WebDriverWait(driver, ConfUtils.getDefaultTimeout()).until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitAlertPresent(WebDriver driver, int timeoutInSecond) {
        new WebDriverWait(driver, timeoutInSecond).until(ExpectedConditions.alertIsPresent());
    }

    public static boolean isElementExists(WebDriver driver, WebElement element, int timeoutInSeconds) {
        try {
            driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
            scrollToElement(driver, element);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(ConfUtils.getDefaultTimeout(), TimeUnit.SECONDS);
        }
    }

    public static boolean checkElementDisplays(WebDriver driver, By by, int timeoutInSeconds) {
        try {
            driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
            scrollToElement(driver, driver.findElement(by));
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(ConfUtils.getDefaultTimeout(), TimeUnit.SECONDS);
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        Common.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        js.executeScript("window.scrollBy(0, -100)");
    }
}
