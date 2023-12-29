package com.wuweibi.module4j.module;


import com.wuweibi.module4j.SupperModule;
import com.wuweibi.module4j.config.Configuration;
import com.wuweibi.module4j.config.PackageInfo;
import com.wuweibi.module4j.exception.GroovyActivatorLoadException;
import com.wuweibi.module4j.exception.ModuleErrorException;
import com.wuweibi.module4j.exception.ModuleMoveException;
import com.wuweibi.module4j.exception.StopModuleActivatorException;
import com.wuweibi.module4j.groovy.GroovyScriptUtil;
import com.wuweibi.module4j.groovy.ScriptClassLoader;
import com.wuweibi.module4j.listener.InstallListenter;
import com.wuweibi.utils.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 模块容器，用于存取模块
 *
 * @author marker
 * @version 1.0
 */
public class ModuleContext {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(ModuleContext.class);




    /** 线程安全集合 */
    private Map<String, Module> modules = new ConcurrentHashMap<>();



    /**  */
    private Configuration configuration;

    /** 安装监听器 */
    private List<InstallListenter> listenters = new ArrayList<>();


    /** 绑定当前的模块 */
    public static final ThreadLocal<Module> MODULE_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 构造
     *
     * @param config 配置信息
     */
    public ModuleContext(Map<String, String> config) {
        this.configuration = new Configuration(config);


    }


    /**
     * 安装模块
     *
     * @param moduleFilePath 模块路径
     * @return Module
     */
    public Module install(String moduleFilePath) {
        File moduleFile = new File(moduleFilePath);
        if (!moduleFile.exists()) {
            logger.warn("modulefilepath[{}] not exists!", moduleFilePath);
            return null;
        }
        try {
            return install(moduleFile);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 安装模块
     *
     * @param moduleFile 模块文件
     *
     * @return Module
     *
     * @throws IOException 异常
     */
    public Module install(File moduleFile) throws IOException {
        // 校验模块完整性
        if (!moduleFile.isDirectory()) {
            throw new ModuleErrorException();
        }

        // 读取 packageJson
        String packageJson = moduleFile.getAbsolutePath() + File.separator + "package.json";
        logger.info("loading {}", packageJson);


        PackageInfo pageInfo = PackageInfo.parseString(packageJson);



        // 判断上级路径是否是模块目录
        File parentFile  = moduleFile.getParentFile();
        File modulesFile = new File(configuration.getAutoDeployDir());

        // 设置模块的目录名称
        pageInfo.put(Configuration.CONFIG_DIRECTORY, modulesFile.getName());

        if (modulesFile.equals(parentFile)) { // 若路径相同
            logger.debug("install path is modules path!");
        } else { // 路径不同

            // 拷贝文件夹到模块目录
            String moduleId =  pageInfo.getId();

            File toFile = modulesFile;
            try {
                logger.info("file copy....");
                FileTools.copyFiles(moduleFile, toFile);
                String toFile2 = toFile.getPath() + File.separator + moduleFile.getName();
                File file2 = new File(toFile2);
                File file3 = new File(toFile.getPath() + File.separator + moduleId);
                file2.renameTo(file3);
                moduleFile = file3;
            } catch (IOException e) {
                throw new ModuleMoveException(e);
            }
        }


        // 加载 ActivatorFile
        String activatorFile = pageInfo.getActivatorFile();

        String modulePath = moduleFile.getAbsolutePath() + File.separator;



//        ClassPool cpool = ClassPool.getDefault();


        ScriptClassLoader loader = new ScriptClassLoader(modulePath);


        SupperModule activator;
        try {

            logger.debug("start groovy engine...");
//				GroovyScriptEngine gse = new GroovyScriptEngine(src, this.getClass().getClassLoader());

            logger.debug("start load groovy[{}] script...", activatorFile);
            Class clzz = loader.parseClass(activatorFile);
            logger.debug("{}", clzz.getName());

            activator = (SupperModule) clzz.newInstance();


            activator.setUtil(new GroovyScriptUtil(pageInfo, loader));
            activator.setPath(modulePath);

        } catch (Exception e) {
            throw new GroovyActivatorLoadException(e);
        }

        logger.info("build module complete...");

        Module module = new Module(activator, pageInfo, this);



        // 持久化
        modules.put(moduleFile.getName(), module);

        // 监听器调用
        Iterator<InstallListenter> it = listenters.iterator();
        while (it.hasNext()) {
            InstallListenter lis = it.next();
            lis.install(module);
        }
        return module;

    }


    /**
     *  卸载
     * @param id id
     */
    public void uninstall(String id) {
        if (modules.containsKey(id)) {
            try {
                Module module = this.modules.get(id);
                if (module.getStatus() == Module.STATUS_RUNING) {
                    module.stop();
                }
                this.modules.remove(id);
                String directoryName = module.getDirectory();



                // 本地文件删除
                String moduleDir = configuration.getAutoDeployDir() + File.separator + directoryName;

                FileTools.deleteDirectory(new File(moduleDir));
                Iterator<InstallListenter> it = listenters.iterator();
                while (it.hasNext()) {
                    InstallListenter lis = it.next();
                    lis.uninstall(module);
                }


            } catch (StopModuleActivatorException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     *
     * @return Map
     */
    public Map<String, Module> getModules() {
        return modules;
    }


    /**
     *
     * @return List
     */
    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Set<String> sets = modules.keySet();
        for (String uuid : sets) {
            Module m = modules.get(uuid);
            list.add(m.getConfig());
        }
        return list;
    }


    /**
     * 获取当前的模块
     * @return Module
     */
    public Module getModule() {
        return MODULE_THREAD_LOCAL.get();
    }


    /**
     * 添加安装监听器
     * @param listener 监听器
     */
    public void addInstallListener(InstallListenter listener) {
        this.listenters.add(listener);
    }

    /**
     * 设置
     * @param threadLocal 锁
     */
    public void setThreadLocal(Module threadLocal) {
        this.MODULE_THREAD_LOCAL.set(threadLocal);
    }
}
