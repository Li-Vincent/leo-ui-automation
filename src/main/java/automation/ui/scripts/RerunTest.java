package automation.ui.scripts;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import automation.ui.base.BaseTest;
import automation.ui.base.Result;
import automation.utils.ReportLogger;

public class RerunTest extends BaseTest {
    private ReportLogger logger = new ReportLogger(RerunTest.class);

    public RerunTest(String scenario) {
        super("RerunTest", scenario);
    }

    @Test(enabled = true)
    public void Rerun(ITestContext context) throws Exception {
        allTestStep(context);
    }

    protected Result onPreCondition() {
        logger.info("Test onPreCondition");
        return Result.PASS;
    }

    protected Result onTest() {
        logger.info("Test onTest");
        return Result.FAIL;
    }

    protected Result onPostCondition() {
        logger.info("Test onPostCondition");
        return Result.PASS;
    }
}
