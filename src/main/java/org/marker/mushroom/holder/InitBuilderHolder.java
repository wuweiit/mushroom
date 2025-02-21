package org.marker.mushroom.holder;


import com.wuweibi.module4j.ModuleFramework;
import com.wuweibi.module4j.config.Configuration;
import com.wuweibi.module4j.exception.PackageJsonNotFoundException;
import com.wuweibi.module4j.listener.InstallListenter;
import com.wuweibi.module4j.module.Module;
import com.wuweibi.module4j.module.ModuleContext;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.component.SiteContext;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.proxy.SingletonProxyKeyWordComputer;
import org.marker.mushroom.ext.message.MessageDBContext;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.ext.model.impl.*;
import org.marker.mushroom.ext.plugin.PluginContext;
import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.ext.tag.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 环境初始化构建
 *
 *
 * @author marker
 * */
public class InitBuilderHolder implements ServletContextAware{

	/** 日志记录器 */
	protected Logger logger =  LoggerFactory.getLogger(InitBuilderHolder.class);


	@Override
	public void setServletContext(ServletContext application) {
		String webRootPath = WebRealPathHolder.REAL_PATH;//网站根目录路径
		logger.info("mrcms runtime on path = {}", webRootPath);

		logger.info("check mrcms whether install?");
		WebAPP.install = isInstall(webRootPath);// 设置系统是否被安装
		logger.info("check success. install = {}", WebAPP.install);



		/*
		 * ============================================================
		 *          ActionContext bind (application)应用作用域
		 * ============================================================
		 */
		logger.info("bind application context = {}", application);
		ActionContext.currentThreadBindServletContext(application);


		/*
		 * ============================================================
		 *          Database config bind
		 * ============================================================
		 */
		DataBaseConfig.getInstance().init();




		/*
		 * ============================================================
		 *               URLRewrite 初始化URL规则 （通过SPringBean初始化）
		 * ============================================================
		 */
		URLRewriteConfig urlConfig = URLRewriteConfig.getInstance();
		logger.info("build URL-rewriteConfig instance = {}", urlConfig);



		/*
		 * ============================================================
		 *               关键字提取代理，初始化(避免懒加载带来的等待)
		 * ============================================================
		 */
		logger.info("build keyword instance = {}", "");
		SingletonProxyKeyWordComputer.init(webRootPath);// 初始化dic
		SingletonProxyKeyWordComputer.getInstance();



		/*
		 * ============================================================
		 *               内容模型支持
		 * ============================================================
		 */
		ContentModelContext contentModelContext = ContentModelContext.getInstance();

		contentModelContext.put(new ContentModelImpl());// 栏目数据模型
		contentModelContext.put(new ArticleModelImpl());// 文章数据模型
		contentModelContext.put(new DoctorModelImpl());// 医生数据模型
		contentModelContext.put(new CategoryModelImpl());// 科室数据模型
		contentModelContext.put(new ThematicModelImpl());// 专题数据模型
		contentModelContext.put(new ProjectModelImpl());// 项目数据模型






		/*
		 * ============================================================
		 *               插件加载
		 * ============================================================
		 */

		PluginContext pluginContext = PluginContext.getInstance();

		try {
//            pluginContext.put(new GuestBookPluginletImpl());
		} catch (Exception e) {
			logger.error("", e);
		}





		/*
		 * ============================================================
		 *               TaglibContext 初始化
		 * ============================================================
		 */
		logger.info("mrcms taglibs init ...");
		TaglibContext taglibs = TaglibContext.getInstance();

		// 系统内置标签
		taglibs.put(new AbsoluteURLTagImpl());
		taglibs.put(new ExecuteTimeTagImpl());
		taglibs.put(new FormatDateTagImpl());
		taglibs.put(new IfTagImpl());
		taglibs.put(new ListTagImpl());
		taglibs.put(new LoopTagImpl());
		taglibs.put(new OnlineUsersTagImpl());
		taglibs.put(new PluginTagImpl());
		taglibs.put(new SqlExecuteTagImpl());
		taglibs.put(new StringSubTagImpl());
		taglibs.put(new URLRewriteTagImpl());
		taglibs.put(new SingleCategoryTagImpl());
		taglibs.put(new ChildChannelTagImpl());
		taglibs.put(new ListCategoryTagImpl());
		taglibs.put(new SqlPageTagImpl());
		taglibs.put(new NavChildTagImpl());
		taglibs.put(new NavOneTagImpl());


		logger.info("mrcms taglibs init complete");




		/*
		 * ============================================================
		 *               MessageContext 初始化
		 * ============================================================
		 */
		logger.info("mrcms MessageContext init ...");
		if(WebAPP.install){
			MessageDBContext messageDBContext = MessageDBContext.getInstance();
			if(!messageDBContext.isInit()){
				try {
					messageDBContext.init();
				} catch (Exception e) {
					logger.error("", e);
				}
			}

		}


		/*
		 * ============================================================
		 *               缓存 初始化
		 * ============================================================
		 */
		logger.info("mrcms Cache init ...");
		SiteContext siteContext = SpringContextHolder.getBean(SystemStatic.SYSTEM_CMS_SITE);
		siteContext.init();

		if (WebAPP.install) {
			String moduleDir =  webRootPath + "modules";// 模块目录

			// 缓存目录
			String cacheDir = moduleDir + File.separator + "cache";// 模块目录

			Map<String,String> configMap = new HashMap<String,String>();
			// 自动部署目录配置
			configMap.put(Configuration.AUTO_DEPLOY_DIR, moduleDir);
			// 缓存目录
			configMap.put(Configuration.DIR_CACHE, cacheDir);
			// 日志级别
			configMap.put(Configuration.LOG_LEVEL, "1");

			try {
				ModuleFramework moduleFramework = new ModuleFramework(configMap);
				ModuleContext context = moduleFramework.getModuleContext();
				moduleFramework.start();

				context.addInstallListener(new InstallListenter(){

					@Override
					public void uninstall(Module module) {

					}

					@Override
					public void install(Module module) {

					}
				});

				// 停止服务
				//			moduleFramework.stop();

			} catch (PackageJsonNotFoundException e) {
				logger.error("{}", "PackageJsonNotFoundException");
			} catch (Exception e) {
				logger.error("{}", e.getMessage());
			}
		}
		logger.info("init Building complete");

	}


	/**
	 * 判断是否是否已安装(true:安装 false:未安装)
	 * @return boolean 状态
	 * */
	private boolean isInstall(String webRootPath){
		String lockFile = webRootPath + "data" + File.separator + "install.lock";
		logger.info("lockFile={}", lockFile);
		return new File(lockFile).exists();
	}

}
