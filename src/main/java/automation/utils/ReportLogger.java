package automation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
        message = getLogTag() + message;
        logger.info(message);
        Reporter.log(addTimeTag(message));
    }

    public static void logStepToDB(int Step, Object message, String testName, String scenario) {
        if (logToDB.get(scenario + testName) == null) {
            logToDB.put(scenario + testName, "Step " + String.valueOf(Step) + ": " + message + "\n");
        } else {
            logToDB.put(scenario + testName,
                    logToDB.get(scenario + testName) + "Step " + String.valueOf(Step) + ": " + message + "\n");
        }
        logger.info("Step " + String.valueOf(Step) + ": " + message + "\n");
    }

    public void info(String message) {
        message = getLogTag() + message;
        logger.info(message);
        Reporter.log(addTimeTag(message));
    }

    public void debug(String message) {
        message = getLogTag() + message;
        logger.debug(message);
        Reporter.log(addTimeTag(message), 7);
    }

    public void warn(String message) {
        message = getLogTag() + message;
        logger.warn(message);
        Reporter.log(addTimeTag(message), 4);
    }

    public void error(String message) {
        message = getLogTag() + message;
        logger.error(message);
        Reporter.log(addTimeTag(message), 3);
    }

    public void fatal(String message) {
        message = getLogTag() + message;
        logger.fatal(message);
        Reporter.log(addTimeTag(message), 0);
    }

    public void setTestStep(String message) {
        String logTag = getLogTag();
        // add asterisk before and after the message and the whole line will compose of 100 characters
        int asterisk = (100 - logTag.length() - message.length()) / 2;
        String msg = StringUtils.repeat("*", asterisk) + message
                + StringUtils.repeat("*", asterisk + ((100 - message.length()) % 2));
        msg = logTag + msg;
        logger.info(msg);
        Reporter.log(addTimeTag(msg), 6);
    }

    // 根据堆栈信息，拿到调用类的名称、方法名、行号
    private String getLogTag() {
        String logTag = "";

        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        // StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement s = stack[i];
            if (s.getClassName().equals(className)) {
                logTag = "[" + classNameDeal(s.getClassName()) + ":" + s.getMethodName() + ":" + s.getLineNumber()
                        + "] ";
                return logTag;
            }
        }
        return logTag;
    }

    private String addTimeTag(String message) {
        Long timeStamp = System.currentTimeMillis();
        String dateString = timestampToDate(timeStamp);
        return "[" + dateString + "]" + message;
    }

    // 时间戳转date字符串
    private String timestampToDate(Long timestamp) {
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
