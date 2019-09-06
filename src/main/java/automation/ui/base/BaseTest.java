package automation.ui.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import automation.report.dao.AutoReportDao;
import automation.report.dao.AutoReportImpl;
import automation.ui.common.ScreenShot;
import automation.utils.ConfUtils;
import automation.utils.ReportLogger;

public abstract class BaseTest {
    private ReportLogger logger = new ReportLogger(BaseTest.class);

    public WebDriver driver = null;
    public JavascriptExecutor js;
    public String testName;
    public String scenario;
    public String environment;
    public int runID;
    public String browser;
    public String logToDB = "";

    private int testResult; // 5=Not Run; 0=Pass; 1=Fail; 4=Need Manual Check; 6=No Data
    private String testStartTime;
    private String testEndTime;
    private String pictureId = "";
    private static AutoReportDao autoReportService;
    private SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");

    public BaseTest(String testName) {
        this.testName = testName;
        this.scenario = "default";
    }

    public BaseTest(String testName, String scenario) {
        this.testName = testName;
        this.scenario = scenario;
    }

    @BeforeSuite
    public void beforeSuite(ITestContext context) throws Exception {
        logger.setTestStep("@BeforeSuite");
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        logger.setTestStep("@BeforeClass");
        this.environment = System.getProperty("environment");
        this.runID = Integer.parseInt(System.getProperty("runId"));
        this.browser = System.getProperty("browser");
        testStartTime = df.format(new Date());
    }

    public void setupBrowser() {
        logger.setTestStep("setupBrowser");
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
        driver.manage().timeouts().pageLoadTimeout(ConfUtils.getDefaultPageTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(ConfUtils.getDefaultElementTimeout(), TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public void prepareTest() {
        testResult = Result.NOTRUN.val();
        logToDB = "";
        setupBrowser();
    }

    @AfterClass(alwaysRun = true)
    public void onClose() {
        logger.setTestStep("@AfterClass");
        testEndTime = df.format(new Date());
        if (ConfUtils.useReportDB()) {
            logToDB = ReportLogger.logToDBMap.get(testName + "-" + scenario);
            insertResultToDB();
        }
    }

    @AfterSuite
    public void afterSuite(ITestContext context) throws Exception {
        logger.setTestStep("@AfterSuite");
    }

    private void insertResultToDB() {
        logger.setTestStep("Insert Test Result to DB");
        if (autoReportService == null) {
            autoReportService = AutoReportImpl.createInstance(ConfUtils.getConf(this.environment));
        }
        String sql = "UPDATE test_case SET " + "start_time = '" + testStartTime + "', end_time = '" + testEndTime
                + "', test_result = " + testResult + ", test_log = ?, environment = '" + this.environment
                + "' WHERE test_runid = " + this.runID + " and case_name = '" + this.testName + "' and scenario = '"
                + this.scenario + "'";
        autoReportService.update(sql, logToDB);
    }

    protected void allTestStep(ITestContext context) throws Exception {
        // Set UniqueTestKey to ITestResult.
        Reporter.getCurrentTestResult().setAttribute(ConfUtils.getUniqueTestKey(), testName + "-" + scenario);
        logger.info("Start to run test: " + testName + " - " + scenario);
        try {
            logger.info("onPreCondition");
            if (onPreCondition().failed()) {
                logger.error("TEST SKIPPED");
                throw new SkipException("PRECONDITION FAILURE");
            }
            logger.info("onTest");
            if (onTest().failed()) {
                logger.error("TEST FAILED");
                throw new InterruptedException("TEST FAILURE");
            }
            logger.info("onPostCondition");
            if (onPostCondition().failed()) {
                logger.warn("CLEAN UP FAILED");
            }
        } catch (SkipException e) {
            this.testResult = Result.SKIP.val();
            handleException(e);
            return;
        } catch (Exception e) {
            this.testResult = Result.FAIL.val();
            handleException(e);
            return;
        }
        logger.info("TEST PASSED");
        closeBrowser();
        this.testResult = Result.PASS.val();
    }

    protected void onError(ITestContext context) {
        logger.info("ON ERROR");
        if (driver != null) {
            pictureId = ScreenShot.takeScreenShot(driver, testStartTime);
            logger.info("ScreenShot PictureId is " + pictureId);
        }
        closeBrowser();
    };

    protected void handleException(Exception e) throws Exception {
        logger.info("Handle Exception");
        logger.error("Exception occur.", e);
        if (driver != null) {
            pictureId = ScreenShot.takeScreenShot(driver, testStartTime);
            logger.info("ScreenShot PictureId is " + pictureId);
        }
        closeBrowser();
        throw e;
    }

    protected void closeBrowser() {
        if (driver != null) {
            try {
                driver.manage().deleteAllCookies();
                driver.quit();
            } catch (Exception e) {
                logger.error("WebDriver has a trouble");
            }
        }
    }

    protected abstract Result onPreCondition();

    protected abstract Result onTest();

    protected abstract Result onPostCondition();

}
