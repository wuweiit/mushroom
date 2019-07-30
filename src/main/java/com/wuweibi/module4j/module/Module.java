package com.wuweibi.module4j.module;


import com.alibaba.fastjson.JSONObject;
import com.wuweibi.module4j.ModuleActivator;
import com.wuweibi.module4j.config.PackageInfo;
import com.wuweibi.module4j.exception.StartModuleActivatorException;
import com.wuweibi.module4j.exception.StopModuleActivatorException;

import java.io.Serializable;


/**
 * 模块对象
 *
 * @author marker
 * @version 1.0
 */
public class Module implements Serializable {
    private static final long serialVersionUID = 2740932792002272914L;


    /** ID */
    public static final String CONFIG_ID = "id";

    /** 状态 */
    public static final String CONFIG_STATUS = "status";

    /** 目录 */
    public static final String CONFIG_DIRECTORY = "directory";


    /**
     * 准备状态
     */
    public static final int STATUS_READY = 0;

    /**
     * 运行状态
     */
    public static final int STATUS_RUNING = 1;

    /**
     * 停止状态
     */
    public static final int STATUS_STOP = 2;

    /**
     * 错误状态
     */
    public static final int STATUS_ERROR = 3;


    /**  */
    private ModuleActivator activator;

    /** 配置信息  */
    private PackageInfo config;

    /** 上下文对象 */
    private ModuleContext context;

    /** 状态 */
    private int status = STATUS_READY;



    /**
     * 构造
     * @param context 上下文
     */
    public Module(ModuleContext context) {
        this.context = context;
    }


    /**
     * 构造2
     * @param activator 启动器
     * @param config 配置
     * @param context 上下文
     */
    public Module(ModuleActivator activator, PackageInfo config,
                  ModuleContext context) {
        super();
        this.activator = activator;
        this.config = config;
        this.context = context;
    }


    /**
     * 启用
     *
     * @throws StartModuleActivatorException 异常
     */
    public void start() throws StartModuleActivatorException {
        synchronized (Module.class) {
            if (status == STATUS_READY || status == STATUS_STOP) {
                context.setThreadLocal(this); // 绑定当前模块
                activator.start(context);
                status = STATUS_RUNING;
                config.put("status", status);
            }
        }
    }


    /**
     * 停用
     *
     * @throws StopModuleActivatorException 异常
     */
    public void stop() throws StopModuleActivatorException {
        synchronized (Module.class) {
            if (status == STATUS_RUNING) {
                context.setThreadLocal(this); // 绑定当前模块
                activator.stop(context);
                status = STATUS_STOP;
                config.put("status", status);
            }
        }
    }



    /**
     *
     * 获取配置信息
     *
     * @return JSONObject 配置
     */
    public JSONObject getConfig() {
        config.put(Module.CONFIG_STATUS, this.status);
        return config;
    }



    /**
     * 获取模块状态
     *
     * @return int
     */
    public int getStatus() {
        return status;
    }



    /**
     * 获取模块唯一标记
     *
     * @return String
     */
    public String getId() {
        return this.config.getString(CONFIG_ID);
    }


    /**
     * 获取模板的目录名称
     *
     * @return String
     */
    public String getDirectory() {
        return config.getString(CONFIG_DIRECTORY);
    }
}
