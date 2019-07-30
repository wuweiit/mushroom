package com.wuweibi.module4j.listener;
/**
 * Created by marker on 2017/8/20.
 */


import com.wuweibi.module4j.module.Module;

/**
 * 安装监听器
 *
 * @author marker
 *  2017-08-20 上午8:58
 **/
public interface InstallListenter {



    /**
     * 卸载监听
     * @param module 模块信息
     */
    void uninstall(Module module);

    /**
     * 安装监听
     * @param module 模块信息
     */
    void install(Module module);
}
