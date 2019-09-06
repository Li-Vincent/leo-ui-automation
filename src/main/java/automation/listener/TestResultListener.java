package automation.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import automation.utils.ConfUtils;

public class TestResultListener extends TestListenerAdapter {

    private ReportLogger logger = new ReportLogger(TestResultListener.class);

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);

        // List of test results which we will delete later
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
        // collect all id's from passed test
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            logger.logNotToDB("PassedTest = " + passedTest.getAttribute(ConfUtils.getUniqueTestKey()));
            passedTestIds.add(getTestId(passedTest));
        }

        // Record the repeated failed Results
        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            logger.logNotToDB("failedTest = " + failedTest.getAttribute(ConfUtils.getUniqueTestKey()));
            // id = class + method + dataProvider
            int failedTestId = getTestId(failedTest);
            logger.logNotToDB("failedTestId = " + failedTestId);

            // if we saw this test as a failed test before we mark as to be deleted
            // or delete this failed test if there is at least one passed version
            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }

        // Record the repeated skipped Results
        Set<Integer> skipTestIds = new HashSet<Integer>();
        for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
            int skipTestId = getTestId(skipTest);
            if (skipTestIds.contains(skipTestId) || passedTestIds.contains(skipTestId)
                    || failedTestIds.contains(skipTestId)) {
                testsToBeRemoved.add(skipTest);
            } else {
                skipTestIds.add(skipTestId);
            }
        }

        // finally delete all tests that are recorded
        // delete skipped tests
        for (Iterator<ITestResult> iterator = testContext.getSkippedTests().getAllResults().iterator(); iterator
                .hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                logger.logNotToDB("Remove repeat Skip Test: " + testResult.getAttribute(ConfUtils.getUniqueTestKey()));
                iterator.remove();
            }
        }
        // delete failed tests
        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator
                .hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                logger.logNotToDB("Remove repeat Fail Test: " + testResult.getAttribute(ConfUtils.getUniqueTestKey()));
                iterator.remove();
            }
        }
    }

    private int getTestId(ITestResult result) {
        // id = uniqueTestKey + class + method + dataProvider
        int id = result.getAttribute(ConfUtils.getUniqueTestKey()).hashCode();
        id = id + result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }
}
