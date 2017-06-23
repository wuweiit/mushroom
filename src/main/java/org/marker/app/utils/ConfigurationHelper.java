package org.marker.app.utils;

/**
 * 读取配置文件工具类
 *
 * @author marker
 * Created by marker on 2015/6/24.
 */

import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class ConfigurationHelper {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationHelper.class);

    private ConfigurationHelper() {
    }

    private static final String filePath = "/etc/hsxycms/config.properties";

    private static volatile Properties property = getPropertyInstance();

    private static Properties getPropertyInstance() {
        if (property == null) {
            synchronized (ConfigurationHelper.class) {
                if (property == null) {
                    property = new Properties();
                    try (FileInputStream loadSettingStream = new FileInputStream(filePath)) {
                        property.load(loadSettingStream);
                        log.info("load properties completed ...");
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return property;
    }

    public static String getProperty(String key) {
        return property.getProperty(key);
    }

    public static String getReverseProperty(String value, boolean fuzzy) {
        String result = "";
        Set<Map.Entry<Object, Object>> propertiesEntry = property.entrySet();
        for (Iterator<Map.Entry<Object, Object>> iterator = propertiesEntry.iterator(); iterator.hasNext(); ) {
            Map.Entry<Object, Object> entry = iterator.next();
            String keyInEntry = (String) entry.getKey();
            String valueInEntry = (String) entry.getValue();

            if (StringUtils.isNullOrEmpty(valueInEntry) || StringUtils.isNullOrEmpty(keyInEntry)) {
                continue;
            }

            if (fuzzy) {
                if (valueInEntry.equals(value) || valueInEntry.contains(value) || value.contains(valueInEntry)) {
                    result = keyInEntry;
                    break;
                }
            } else {
                if (valueInEntry.equals(value)) {
                    result = keyInEntry;
                    break;
                }
            }
        }
        return result;
    }

    public static String getReverseProperty(String value) {
        return getReverseProperty(value, false);
    }


    /**
     * 即时获取配置文件
     * @param key
     * @return
     */
    public static String getDynamicProperty(String key) {
        Properties property = new Properties();
        try (FileInputStream loadSettingStream = new FileInputStream(filePath)) {
            property.load(loadSettingStream);
            log.info("load properties completed ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property.getProperty(key);
    }

}
