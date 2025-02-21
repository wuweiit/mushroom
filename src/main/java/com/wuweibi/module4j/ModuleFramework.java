package com.wuweibi.module4j;


import com.wuweibi.module4j.config.Configuration;
import com.wuweibi.module4j.exception.GroovyActivatorLoadException;
import com.wuweibi.module4j.exception.PackageJsonNotFoundException;
import com.wuweibi.module4j.exception.StartModuleActivatorException;
import com.wuweibi.module4j.exception.StopModuleActivatorException;
import com.wuweibi.module4j.module.Module;
import com.wuweibi.module4j.module.ModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.Set;


/**
 * 模块化框架
 *
 * @author marker
 * @version 1.0
 */
public class ModuleFramework {

    /** 日志 */
    private final Logger logger = LoggerFactory.getLogger(ModuleFramework.class);





    /***
     * 模块上下文对象
     */
    private ModuleContext context;


    /** 配置信息 */
    private Configuration configuration;


    /**
     * 构造
     * @param config 配置
     * @throws Exception 异常
     */
    public ModuleFramework(Map<String, String> config) throws Exception {
        this.configuration = new Configuration(config);
        context = new ModuleContext(config);
    }


    /**
     * 获取模块上下文对象
     *
     * @return ModuleContext 上下文
     */
    public ModuleContext getModuleContext() {
        return context;
    }


    /**
     * 启动
     *
     * @throws Exception 异常
     */
    public void start() throws Exception {
        String modulesDir = configuration.getAutoDeployDir();

        if (null == modulesDir) {
            logger.warn("not found config:{}", Configuration.AUTO_DEPLOY_DIR);
        } else {
            // 扫描自动部署模块
            File file = new File(modulesDir);
            logger.debug("start scan [{}] modules... ", modulesDir);
            for (File moduleFile : file.listFiles()) {
                if (!moduleFile.isDirectory()){ // 检测是否是文件夹
                    logger.warn("this {} is not directory! please check config: {}",
                            file.getAbsolutePath(), Configuration.AUTO_DEPLOY_DIR);
                    continue;
                }

                String fileName = moduleFile.getName(); // 类型
                logger.debug("loading {} ", fileName);
                try {
                    Module module = context.install(moduleFile);
                    if (module != null) {
                        module.start(); // 启动模块
                    }
                } catch (PackageJsonNotFoundException e) {
                    logger.error("module package.json file not found!", e);
                } catch (GroovyActivatorLoadException e) {
                    logger.error("load module [ " + moduleFile.getName() + " ] Activator faild!", e);
                } catch (StartModuleActivatorException e) {
                    logger.error("start module [ " + moduleFile.getName() + " ] faild!", e);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }


        }
    }


    /**
     * 停止服务
     *
     * @throws StopModuleActivatorException 异常
     */
    public void stop() throws StopModuleActivatorException {
        logger.info("stop ModuleFramework...");
        Map<String, Module> modules = context.getModules();
        Set<String> sets = modules.keySet();
        for (String id : sets) {
            logger.info("stop module ({})...", id);
            Module m = modules.get(id);
            m.stop();
        }
        logger.info("stop ModuleFramework complete");
    }

}
