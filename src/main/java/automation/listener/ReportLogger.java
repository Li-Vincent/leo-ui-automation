package automation.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Reporter;

import automation.utils.ConfUtils;

public class ReportLogger {
    public static Map<String, String> logToDBMap = new ConcurrentHashMap<String, String>();

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
        logToDB(addTimeTag(message));
    }

    private void logToDB(String message) {
        if (ConfUtils.useReportDB()) {
            String currentTestKey = Reporter.getCurrentTestResult().getAttribute(ConfUtils.getUniqueTestKey())
                    .toString();
            if (!logToDBMap.containsKey(currentTestKey)) {
                logToDBMap.put(currentTestKey, message + "\n");
            } else {
                logToDBMap.put(currentTestKey, logToDBMap.get(currentTestKey) + message + "\n");
            }
        }
    }

    public void info(String message) {
        message = getLogTag() + message;
        logger.info(message);
        Reporter.log(addTimeTag(message));
        logToDB(addTimeTag(message));
    }

    public void logNotToDB(String message) {
        message = getLogTag() + message;
        logger.info(message);
        Reporter.log(addTimeTag(message));
    }

    public void debug(String message) {
        message = getLogTag() + message;
        logger.debug(message);
        Reporter.log(addTimeTag(message), LogLevel.DEBUG.val());
        logToDB(addTimeTag(message));
    }

    public void warn(String message) {
        message = getLogTag() + message;
        logger.warn(message);
        Reporter.log(addTimeTag(message), LogLevel.WARN.val());
        logToDB(addTimeTag(message));
    }

    public void error(String message) {
        message = getLogTag() + message;
        logger.error(message);
        Reporter.log(addTimeTag(message), LogLevel.ERROR.val());
        logToDB(addTimeTag(message));
    }

    public void error(String message, Exception e) {
        String msg = message;
        if (e instanceof Exception) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            msg += " Reason:" + sw.toString();
        } else {
            msg += " Reason:" + e.getMessage();
        }
        // message = getLogTag() + message;
        logger.error(message, e);
        logToDB(msg);
    }

    public void fatal(String message) {
        message = getLogTag() + message;
        logger.fatal(message);
        Reporter.log(addTimeTag(message), LogLevel.FATAL.val());
        logToDB(addTimeTag(message));
    }

    public void setTestStep(String message) {
        String logTag = getLogTag();
        // add asterisk before and after the message and the whole line will compose of 100 characters
        int asterisk = (100 - logTag.length() - message.length()) / 2;
        String msg = StringUtils.repeat("*", asterisk) + message
                + StringUtils.repeat("*", asterisk + ((100 - message.length()) % 2));
        msg = logTag + msg;
        logger.info(msg);
        // Reporter.log(addTimeTag(msg), LogLevel.INFO.val());
    }

    // 根据堆栈信息，拿到调用类的名称、方法名、行号
    private String getLogTag() {
        String logTag = "";

        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        // StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement s = stack[i];

            if (s.getClassName().equals(className)) {
                logTag = "[" + getSimpleClassName(s.getClassName()) + ":" + s.getMethodName() + ":" + s.getLineNumber()
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
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    // 去掉包名，只保留类名
    private String getSimpleClassName(String allName) {
        String[] className = allName.split("\\.");
        return className[className.length - 1];
    }

}
