package com.wuweibi.module4j;/**
 * Created by marker on 2017/8/19.
 */

import com.wuweibi.module4j.groovy.GroovyScriptUtil;
import com.wuweibi.module4j.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


/**
 *
 *
 *
 * @author marker
 *  2017-08-19 下午11:47
 **/
public abstract class SupperModule  implements ModuleActivator {

    /** 日志记录 */
    private final Logger logger = LoggerFactory.getLogger(ModuleUtils.class);

    /**  脚本工具 */
    private GroovyScriptUtil util;

    /** 路径  */
    private String path;
    /** 模块基本信息 */
    protected Module module;


    /**
     *
     * @param path 路径
     * @return Class
     * @throws Exception 异常
     */
    public Class require(String path) throws Exception {
        return util.loadGroovy("src" + File.separator + path);
    }


    public GroovyScriptUtil getUtil() {
        return util;
    }

    public void setUtil(GroovyScriptUtil util) {
        this.util = util;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 设置模块信息
     * @param module 模块
     */
    public void setModule(Module module) {
        this.module = module;
    }
}
