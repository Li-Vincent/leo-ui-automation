package automation.ui.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import automation.report.dao.AutoReportDao;
import automation.report.dao.AutoReportImpl;

public class ScreenShot {
    private static AutoReportDao autoReportService;

    public static InputStream takeScreenShot(WebDriver driver) {
        ByteArrayInputStream bis = new ByteArrayInputStream(
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        return bis;
    }

    public static String streamToFile(InputStream inputStream) {
        String dir = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory() + "\\..\\html\\img\\";
        long filename = getPictureId();
        String filepath = dir + filename + ".jpg";
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(filepath));
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        Reporter.log("<img class='pimg' src='img/" + filename + ".jpg' width=100 />");
        return filepath;
    }

    // public static String storeImage(WebDriver driver, String pic_id, String url, String createTime, String DB) {
    // boolean flag = false;
    // System.out.println("pic_id:" + pic_id);
    //
    // if (testCaseService == null) {
    // testCaseService = AutoReportImpl.creatInstance(DB);
    // }
    //
    // try {
    // File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    // FileInputStream fin = new FileInputStream(srcFile);
    // ByteBuffer nbf = ByteBuffer.allocate((int) srcFile.length());
    // byte[] array = new byte[1024];
    // // int offset = 0;
    // int length = 0;
    // while ((length = fin.read(array)) > 0) {
    // if (length != 1024)
    // nbf.put(array, 0, length);
    // else
    // nbf.put(array);
    // }
    // fin.close();
    // byte[] content = nbf.array();
    // flag = testCaseService.insertImage(pic_id, content, url, startTime);
    // if (flag == true) {
    // return pic_id;
    // } else {
    // return "";
    // }
    // } catch (Exception e) {
    // Dailylog.logError(e.getMessage(), e);
    // }
    // if (flag == true) {
    // return pic_id;
    // } else {
    // return "";
    // }
    // }

    /**
     * %后的1指第一个参数，若只有var一个可变参数，所以就是指var。 $后的0表示，位数不够用0补齐，如果没有这个0（如%1$nd）就以空格补齐，
     * 0后面的n表示总长度，总长度可以可以是大于9例如（%1$010d），d表示将var按十进制转字符串，长度不够的话用0或空格补齐。
     */
    public static long getPictureId() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL", date);
        return Long.parseLong(str) + 1;// thread safety so add one
    }
}
