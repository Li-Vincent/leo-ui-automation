package automation.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import automation.utils.ReportLogger;

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
            logger.info("PassedTest = " + passedTest.getName());
            passedTestIds.add(getTestScenario(passedTest));
        }

        // Eliminate the repeat failed methods
        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            logger.info("failedTest = " + failedTest.getAttribute("testScenario"));
            // id = class + method + dataprovider
            int failedTestId = getTestScenario(failedTest);
            logger.info("failedTestId = " + failedTestId);

            // if we saw this test as a failed test before we mark as to be
            // deleted
            // or delete this failed test if there is at least one passed
            // version
            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }

        // Eliminate the repeat methods
        Set<Integer> skipTestIds = new HashSet<Integer>();
        for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
            logger.info("skipTest = " + skipTest.getAttribute("testScenario"));
            // id = class + method + dataprovider
            int skipTestId = getTestScenario(skipTest);
            logger.info("skipTestId = " + skipTestId);

            if (skipTestIds.contains(skipTestId) || passedTestIds.contains(skipTestId)
                    || failedTestIds.contains(skipTestId)) {
                testsToBeRemoved.add(skipTest);
            } else {
                skipTestIds.add(skipTestId);
            }
        }
        // finally delete all tests that are marked
        for (Iterator<ITestResult> iterator = testContext.getSkippedTests().getAllResults().iterator(); iterator
                .hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                logger.info("Remove repeat Skip Test: " + testResult.getAttribute("testScenario"));
                iterator.remove();
            }
        }

        // finally delete all tests that are marked
        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator
                .hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                logger.info("Remove repeat Fail Test: " + testResult.getAttribute("testScenario"));
                iterator.remove();
            }
        }
    }

    private int getTestScenario(ITestResult result) {
        System.out.println(result.getAttribute("testScenario"));
        int id = result.getAttribute("testScenario").hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

    @SuppressWarnings("unused")
    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }
}
