package com.wuweibi.module4j.config;

import java.util.Collections;
import java.util.Map;

/**
 * 配置对象
 * 用于全局的配置，定义模块路径相关信息。
 *
 * @author marker
 *
 *         Created by ROOT on 2016/10/27.
 */
public final class Configuration {

    /**
     * 模块上下文名称
     */
    public static final String MODULE_CONTEXT = "mrcms_moduleContext";

    /**
     * 自动部署目录
     */
    public static final String AUTO_DEPLOY_DIR = "MSEI.auto.deploy.dir";


    /**
     * 缓存目录
     */
    public static final String DIR_CACHE = "MSEI.cache.rootdir";

    /**
     * 日志级别
     */
    public static final String LOG_LEVEL = "MSEI.log.level";

    /**
     * 默认activator加载类
     */
    public static final String CONFIG_ACTIVATOR = "MSEI.activator.groovy";

    /**
     * 配置模块目录
     */
    public static final String CONFIG_DIRECTORY = "MSEI.directory.name";

    /**
     * 配置数据
     */
    private Map<String, String> config;


    /**
     * 构造
     * @param config 配置Map
     */
    public Configuration(Map<String, String> config) {
        this.config = Collections.synchronizedMap(config);
    }


    /**
     * 获取模板部署目录
     *
     * @return String
     */
    public String getAutoDeployDir() {
        String path = config.get(AUTO_DEPLOY_DIR);
        if (null == path) {
            return "./modules";
        }
        return path;
    }


    /**
     * 获取 激活器类 脚本 文件类型
     *
     * @return String
     */
    public String getActivatorFile() {
        String path = config.get(CONFIG_ACTIVATOR);
        if (null == path) {
            return "./src/activator.groovy";
        }
        return path;
    }


}
