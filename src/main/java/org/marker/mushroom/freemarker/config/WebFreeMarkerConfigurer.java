package org.marker.mushroom.freemarker.config;

import freemarker.cache.MultiTemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * freemarker模板引擎配置
 *
 *   好像是在页面静态化得时候使用的。
 *
 *
 * FreeMarkerConfigurer 在SpringMVC中只能配置一个，由于后台与前台的配置需要区分。
 * 过包装类对接前端的FreeMarkerConfigurer
 * 
 *
 * @author marker
 * @version 1.0
 */
public class WebFreeMarkerConfigurer implements InitializingBean,ResourceLoaderAware, ServletContextAware {


    /** FreeMarkerConfigurer */
    private FreeMarkerConfigurer freeMarkerConfig = new FreeMarkerConfigurer();



    public Configuration getConfiguration() {
        return freeMarkerConfig.getConfiguration();
    }


    /**
     * 设置多个模板加载路径
     *
     * @param templateLoaderPaths
     */
    public void setTemplateLoaderPaths(String... templateLoaderPaths) {
        freeMarkerConfig.setTemplateLoaderPaths(templateLoaderPaths);
    }

    public void setPreTemplateLoaders(freemarker.cache.StringTemplateLoader preTemplateLoaders) {
        freeMarkerConfig.setPreTemplateLoaders(preTemplateLoaders);
    }

    public void setFreemarkerSettings(java.util.Properties freemarkerSettings) {
        freeMarkerConfig.setFreemarkerSettings(freemarkerSettings);
    }

    public void setFreemarkerVariables(java.util.Map freemarkerVariables) {
        freeMarkerConfig.setFreemarkerVariables(freemarkerVariables);
    }


    /**
     * 配置完成调用
     *
     * @throws IOException
     * @throws TemplateException
     */
    public void afterPropertiesSet() throws IOException, TemplateException {
        freeMarkerConfig.afterPropertiesSet();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        freeMarkerConfig.setResourceLoader(resourceLoader);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        freeMarkerConfig.setServletContext(servletContext);
    }


    /**
     * 整合加载路径
     *
     * @param templateFilePath
     */
    public void mergetemplateLoaderPath(String templateFilePath) {


//        freeMarkerConfig.setTemplateLoaderPaths(templateFilePath);



    }
}

