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

    public static String getConf(String key) {
        return getConfigurations().getProperty(key);
    }
}
