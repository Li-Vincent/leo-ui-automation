package automation.ui.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import automation.report.dao.AutoReportDao;
import automation.report.dao.AutoReportImpl;
import automation.utils.ConfUtils;

public class ScreenShot {
    private static AutoReportDao autoReportService;

    private static byte[] takeScreenShot(WebDriver driver) {
        byte[] bytes = new byte[1024];
        try {
            bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static String storeImageToFile(byte[] bytes, String picID) {
        String dir = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory() + "\\..\\html\\img\\";
        String filepath = dir + picID + ".jpg";
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            FileUtils.copyInputStreamToFile(inputStream, new File(filepath));
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        Reporter.log("<img class='pimg' src='img/" + picID + ".jpg' width=100 />");
        return filepath;
    }

    private static boolean storeImageToDB(WebDriver driver, byte[] bytes, String picID, String createTime) {
        if (autoReportService == null) {
            autoReportService = AutoReportImpl.createInstance(ConfUtils.getConf(System.getProperty("environment")));
        }
        return autoReportService.insertImage(picID, bytes, driver.getCurrentUrl(), createTime);
    }

    public static String takeScreenShot(WebDriver driver, String createTime) {
        byte[] bytes = takeScreenShot(driver);
        String picID = getPictureId();
        storeImageToFile(bytes, picID);
        if (ConfUtils.useReportDB()) {
            storeImageToDB(driver, bytes, picID, createTime);
        }
        return picID;
    }

    /**
     * %后的1指第一个参数，若只有var一个可变参数，所以就是指var。 $后的0表示，位数不够用0补齐，如果没有这个0（如%1$nd）就以空格补齐，
     * 0后面的n表示总长度，总长度可以可以是大于9例如（%1$010d），d表示将var按十进制转字符串，长度不够的话用0或空格补齐。
     */
    public static String getPictureId() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", date);
        return String.valueOf(Long.parseLong(str) + 1);// thread safety so add one
    }
}
