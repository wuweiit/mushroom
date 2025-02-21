package com.wuweibi.module4j.groovy;

import com.wuweibi.module4j.module.Module;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.IOException;
import java.util.Map;


/**
 * Groovy 脚本工具
 *
 * @author marker
 */
public final class GroovyScriptUtil {

    /**
     * 模块配置信息
     */
    private Map<String, Object> moduleConfig;
    /**
     * 脚本类加载器
     */
    private ScriptClassLoader loader;


    /**
     * 构造
     *
     * @param config 配置
     * @param loader 加载器
     */
    public GroovyScriptUtil(Map<String, Object> config, ScriptClassLoader loader) {
        this.moduleConfig = config;
        this.loader = loader;
    }


    /**
     * 加载脚本类
     *
     * @param groovyFile 脚本
     * @return Class
     * @throws CompilationFailedException 异常
     * @throws IOException 异常
     * @throws ClassNotFoundException 异常
     */
    public Class<?> loadGroovy(String groovyFile) throws CompilationFailedException, IOException, ClassNotFoundException {
        Class<?> groovyClass = loader.parseClass(groovyFile);

        // 注入require


        return groovyClass;
    }


    /**
     * 加载模型对象
     *
     * @param groovyName 脚本名称
     *
     * @return GroovyObject
     *
     * @throws Exception 异常
     */
    public GroovyObject loadModelObject(String groovyName) throws Exception {
        GroovyObject obj = null;
        try {
            Class<?> groovyClass = loadGroovy(groovyName);
            obj = (GroovyObject) groovyClass.newInstance();
            obj.invokeMethod("setModule", moduleConfig.get(Module.CONFIG_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 加载标签对象
     *
     * @param groovyName 脚本名称
     * @return GroovyObject
     */
    public GroovyObject loadTagObject(String groovyName) {
        GroovyObject obj = null;
        try {
            Class<?> groovyClass = loadGroovy(groovyName);
            obj = (GroovyObject) groovyClass.newInstance();
            obj.invokeMethod("setModule", moduleConfig.get(Module.CONFIG_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
