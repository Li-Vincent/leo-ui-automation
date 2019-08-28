package automation.ui.testSample;

import java.util.List;

import org.testng.annotations.Test;

import automation.report.dao.AutoReportDao;
import automation.report.dao.AutoReportImpl;
import automation.utils.ConfUtils;

public class TestAutoReport {
    public static AutoReportDao autoReportService;

    @Test
    public void test() {
        if (autoReportService == null) {
            autoReportService = AutoReportImpl.createInstance(ConfUtils.getConf(System.getProperty("environment")));
        }

        List<String> caseNameList = autoReportService.getCaseList();
        for (int i = 0; i < caseNameList.size(); i++) {
            System.out.println(caseNameList.get(i));
        }
        assert true;
    }
}
