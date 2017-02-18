package org.marker.app.utils;

/**
 * 环境工具
 *
 * @author marker
 * Created by Administrator on 2016/12/14.
 */
public class EnvUtils {


    private static boolean dev;

    /**
     * 是否生产环境
     * @return
     */
    public static boolean isProduction(){
        String env_name = ConfigurationHelper.getProperty("env_name");
        if("2".equals(env_name)) return true;
        return false;
    }


    /**
     * 获取Msite线上地址
     * @return
     */
    public static String getMsiteAddress(){
        return ConfigurationHelper.getProperty("MSITE_WEB_ADDRESS");
    }


    /**
     * 是否开发环境
     * @return
     */
    public static boolean isDev() {
        String env_name = ConfigurationHelper.getProperty("env_name");
        if("0".equals(env_name)) return true;
        return false;
    }

    /**
     * 是否预发环境
     * @return
     */
    public static boolean isPreProduction(){
        String env_name = ConfigurationHelper.getProperty("env_name");
        if("1".equals(env_name)) return true;
        return false;
    }
}
