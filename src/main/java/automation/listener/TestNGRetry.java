package automation.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import automation.utils.ConfUtils;
import automation.utils.ReportLogger;

/**
 * Customize RetryAnalyzer
 * 
 * @author Vincent-Li
 */
public class TestNGRetry implements IRetryAnalyzer {
    private ReportLogger logger = new ReportLogger(this.getClass());

    private Map<String, Integer> rerunScenarioMap = new ConcurrentHashMap<String, Integer>();
    private static int maxRetryCount = ConfUtils.getMaxRerunTimes(); // 控制失败跑几次

    @Override
    synchronized public boolean retry(ITestResult result) {
        String rerunScenario = result.getAttribute(ConfUtils.getUniqueTestKey()).toString();
        if (!rerunScenarioMap.containsKey(rerunScenario)) {
            rerunScenarioMap.put(rerunScenario, 1);
        }
        int currentTimes = rerunScenarioMap.get(rerunScenario);
        if (currentTimes < maxRetryCount) {
            logger.setTestStep("Rerun Test - " + rerunScenario + " - Times:" + currentTimes);
            rerunScenarioMap.put(rerunScenario, currentTimes + 1);
            // Remove repeated logToDB.
            if (ConfUtils.useReportDB()) {
                ReportLogger.logToDBMap.remove(result.getAttribute(ConfUtils.getUniqueTestKey()).toString());
            }
            return true;
        }
        logger.setTestStep("Rerun Test - " + rerunScenario + " - Times:" + currentTimes);
        return false;
    }
}
