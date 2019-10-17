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

    public static int getDefaultPageTimeout() {
        return Integer.parseInt(getConfigurations().getProperty("DefaultPageTimeout"));
    }

    public static int getDefaultElementTimeout() {
        return Integer.parseInt(getConfigurations().getProperty("DefaultElementTimeout"));
    }

    public static int getMaxRerunTimes() {
        return Integer.parseInt(getConfigurations().getProperty("MaxRerunTimes"));
    }

    public static String getUniqueTestKey() {
        return getConfigurations().getProperty("UniqueTestKey");
    }

    public static String getConf(String key) {
        return getConfigurations().getProperty(key);
    }

    public static boolean useReportDB() {
        return System.getProperty("useReportDB").toLowerCase().contains("true")
                || System.getProperty("useReportDB").toLowerCase().contains("yes");
    }

}
