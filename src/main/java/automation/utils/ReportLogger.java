package automation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Reporter;

public class ReportLogger {
    public static Map<String, String> logToDB = new HashMap<String, String>();
    /**
     * Report Verbose Level OFF = 0, FATAL = 0, ERROR = 3, WARN = 4, DEBUG = 6, INFO = 7, TRACE = 7, ALL = 10
     */
    private String className;
    private static Logger logger;

    public ReportLogger(Class<?> clazz) {
        logger = Logger.getLogger(clazz);
        className = clazz.getName();
    }

    public void log(String message) {
        logger.info(message);
        message = getLogTag() + message;
        Reporter.log(message);
    }

    public static void logStepToDB(int Step, Object message, String store, String testName) {
        if (logToDB.get(store + testName) == null) {
            logToDB.put(store + testName, "Step " + String.valueOf(Step) + ": " + message + "\n");
        } else {
            logToDB.put(store + testName,
                    logToDB.get(store + testName) + "Step " + String.valueOf(Step) + ": " + message + "\n");
        }
        logger.info("Step " + String.valueOf(Step) + ": " + message + "\n");
    }

    public void info(String message) {
        logger.info(message);
        message = getLogTag() + message;
        Reporter.log(message, 6);
    }

    public void debug(String message) {
        logger.debug(message);
        message = getLogTag() + message;
        Reporter.log(message, 7);
    }

    public void warn(String message) {
        logger.warn(message);
        message = getLogTag() + message;
        Reporter.log(message, 4);
    }

    public void error(String message) {
        logger.error(message);
        message = getLogTag() + message;
        Reporter.log(message, 3);
    }

    public void fatal(String message) {
        logger.error(message);
        message = getLogTag() + message;
        Reporter.log(message, 0);
    }

    // 根据堆栈信息，拿到调用类的名称、方法名、行号
    public String getLogTag() {
        String logTag = "";
        Long timeStamp = System.currentTimeMillis();
        String dateString = timestampToDate(timeStamp);
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement s = stack[i];
            if (s.getClassName().equals(className)) {
                logTag = "[" + dateString + "]" + "[" + classNameDeal(s.getClassName()) + ":" + s.getLineNumber() + "]";
            }
        }
        return logTag;
    }

    // 时间戳转date字符串
    public static String timestampToDate(Long timestamp) {
        if (timestamp.toString().length() < 13) {
            timestamp = Long.valueOf(timestamp.toString().substring(0, 10) + "000");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        Date date = new Date(timestamp);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    // 去掉包名，只保留类名
    private String classNameDeal(String allName) {
        String[] className = allName.split("\\.");
        return className[className.length - 1];
    }
}
