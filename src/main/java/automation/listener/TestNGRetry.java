package automation.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import automation.utils.ReportLogger;

public class TestNGRetry implements IRetryAnalyzer {
    private ReportLogger logger = new ReportLogger(this.getClass());

    private Map<String, Integer> rerunScenarioMap = new ConcurrentHashMap<String, Integer>();
    private static int maxRetryCount = 3; // 控制失败跑几次

    @Override
    synchronized public boolean retry(ITestResult result) {
        String rerunScenario = result.getTestContext().getAttribute("rerunScenario").toString();
        if (!rerunScenarioMap.containsKey(rerunScenario)) {
            rerunScenarioMap.put(rerunScenario, 1);
        }
        int currentTimes = rerunScenarioMap.get(rerunScenario);
        if (currentTimes < maxRetryCount) {
            logger.setTestStep("Rerun Test - " + rerunScenario + " - Times:" + currentTimes);
            rerunScenarioMap.put(rerunScenario, currentTimes + 1);
            return true;
        }
        logger.setTestStep("Rerun Test - " + rerunScenario + " - Times:" + currentTimes);
        return false;
    }
}
