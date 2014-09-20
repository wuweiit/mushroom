package org.marker.ext;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.ext.exception.GroovyActivatorLoadException;
import org.marker.ext.exception.PackageJsonNotFoundException;
import org.marker.ext.exception.StartModuleActivatorException;
import org.marker.ext.module.Module;
import org.marker.ext.module.ModuleContext;


/**
 * 
 * @author marker
 * @version 1.0
 */
public class Msei {
	
	private final Log log = LogFactory.getLog(Msei.class);
	
	// 模块上下文名称
	public final static String MODULE_CONTEXT = "mrcms_moduleContext";
	
	// 自动部署目录
	public final static  String DIR_MODULES  = "MSEI.auto.deploy.dir";
	
	// 缓存目录
	public final static String DIR_CACHE = "MSEI.cache.rootdir";
	
	// 日志级别
	public final static String LOG_LEVEL = "MSEI.log.level";
	
	
	
	
	private ModuleContext context = new ModuleContext();
	
	
	
	public Msei(Map<String, String> config) throws Exception { 
		
		String modulesDir = config.get(DIR_MODULES);
//		String modulesCache = config.get(DIR_CACHE);
		
		if(null == modulesDir){
			throw new Exception("配置信息缺失：" + DIR_MODULES);
		}
		
		
		// 扫描模块
        File file = new File(modulesDir);
        
        for(File f : file.listFiles()){
			String uuid = f.getName();//  
			File moduleFile = new File(modulesDir + File.separator + uuid);
			log.info(moduleFile + "load");
			try {
				Module module = context.install(moduleFile);
				module.start();// 启动模块  
			}catch(PackageJsonNotFoundException e){
				log.error("module package.json file not found!", e);
			} catch (GroovyActivatorLoadException e) {
				log.error("load module [ "+moduleFile.getName()+" ] Activator faild!",e);
			} catch (StartModuleActivatorException e) {
				log.error("start module [ "+moduleFile.getName()+" ] faild!",e);
			} catch (Exception e) {
				log.error("",e);
			}
        
        }
	} 

	 
	/**
	 * 获取模块上下文对象
	 * @return
	 */
	public ModuleContext getModuleContext() {
		return context;
	}




	
}
