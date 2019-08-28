package automation.ui.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import automation.report.dao.AutoReportDao;
import automation.report.dao.AutoReportImpl;
import automation.utils.ConfUtils;

public abstract class BaseTest {
    public WebDriver driver;
    public JavascriptExecutor js;
    public String testName = "JIRA_1";
    public String environment;
    public int runID;
    public String browser = "";
    public String log = "";

    private int testResult; // 5=Not Run; 0=Pass; 1=Fail; 4=Need Manual Check; 6=No Data
    private String testStartTime;
    private String testEndTime;
    private String pictureId = "";
    private static AutoReportDao autoReportService;
    private SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");

    @BeforeClass(alwaysRun = true)
    public void setup() {
        this.environment = System.getProperty("environment");
        this.runID = Integer.parseInt(System.getProperty("runId"));
        this.browser = System.getProperty("browser");
        testStartTime = df.format(new Date());
    }

    public void setupBrowser() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
            }
        }
        if (browser != "") {
            switch (browser.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "src/main/resources/driver/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "ie11":
                System.setProperty("webdriver.ie.driver", "src/main/resources/driver/IEDriverServer.exe");
                driver = new InternetExplorerDriver();
                break;
            default:
                System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe");
                driver = new ChromeDriver();
            }
        }
        driver.manage().timeouts().pageLoadTimeout(ConfUtils.getDefaultTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ConfUtils.getDefaultTimeout(), TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public void prepareTest() {
        testResult = 0;
        log = "";
        setupBrowser();
    }

    @AfterClass(alwaysRun = true)
    public void close() {
        testEndTime = df.format(new Date());
        insertResult();

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
            }
        }
    }

    private void insertResult() {
        if (autoReportService == null) {
            autoReportService = AutoReportImpl.createInstance(ConfUtils.getConf(this.environment));
        }
        String sql = "UPDATE test_case SET " + "start_time = '" + testStartTime + "', end_time = '" + testEndTime
                + "', test_result = " + testResult + ", test_log = '" + log + "', environment = '" + this.environment
                + "' WHERE test_runid = " + this.runID + " and case_name = '" + this.testName + "'";
        autoReportService.update(sql);
        System.out.println("insert test result");
    }

}
