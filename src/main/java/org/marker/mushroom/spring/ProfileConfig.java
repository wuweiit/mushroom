package org.marker.mushroom.spring;
/**
 * Created by marker on 2018/11/24.
 */

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 *
 * Spring Profile 配置
 * (解决各环境的配置加载)
 *
 * @author marker
 * @create 2018-11-24 22:26
 **/
public class ProfileConfig implements Serializable{


    /**
     * Spring Profile 默认dev
     */
    private String profile = "dev";

    /**
     * 配置文件路径
     */
    private String config;


    public String getConfig() {
        // 尝试获取 -Dmrcms.config 参数
        String mrcmsConfig = System.getProperty("mrcms.config");
        if (StringUtils.isNotBlank(mrcmsConfig)) {
            this.config = "file:" + mrcmsConfig;
        }
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
