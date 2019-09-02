package automation.utils;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfUtils {

    public static Properties getConfigurations() {
        Properties properties = new Properties();
        try {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("conf.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static int getDefaultTimeout() {
        return Integer.parseInt(getConfigurations().getProperty("DefaultTimeout"));
    }

    public static int getElementRetryTimes() {
        return Integer.parseInt(getConfigurations().getProperty("ElementRetryTimes"));
    }

    public static String getConf(String key) {
        return getConfigurations().getProperty(key);
    }

    public static boolean useReportDB() {
        return System.getProperty("useReportDB").toLowerCase().contains("true")
                || System.getProperty("useReportDB").toLowerCase().contains("yes");
    }
}
