package org.marker.ext.module;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.marker.ext.ModuleActivator;
import org.marker.ext.exception.GroovyActivatorLoadException;
import org.marker.ext.exception.PackageJsonNotFoundException;
import org.marker.ext.exception.StopModuleActivatorException;
import org.marker.ext.groovy.GroovyScriptUtil;
import org.marker.ext.groovy.ScriptClassLoader;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.utils.FileTools;

import com.mongodb.util.JSON;


/**
 * 模块容器，用于存取模块
 * @author marker
 * @version 1.0
 */
public class ModuleContext {

	// 线程安全集合
	private Map<String,Module> modules = new ConcurrentHashMap<String,Module>();
	
	
	public Module install(File moduleFile) throws PackageJsonNotFoundException, GroovyActivatorLoadException{ 
		if(moduleFile.isDirectory()){ 
			// 配置文件路径
			String packageJson = moduleFile.getAbsolutePath() + File.separator + "package.json";

			String json = "{invalid config info}";
			try {
				json = FileTools.getFileContet(new File(packageJson), FileTools.FILE_CHARACTER_UTF8);
			} catch (IOException e) {
				throw new PackageJsonNotFoundException();
			}
			
			@SuppressWarnings("unchecked")
			Map<String,Object> config = (Map<String, Object>) JSON.parse(json);
			config.put(Module.CONFIG_UUID, moduleFile.getName());// 设置模块唯一码
 
			
			String src = moduleFile.getAbsolutePath() + File.separator + "src"; 
			 
			ScriptClassLoader loader = new ScriptClassLoader(src);  
			
			GroovyObject obj;
			try {  
				GroovyScriptEngine gse = new GroovyScriptEngine(src, this.getClass().getClassLoader());
				
				
				
				Class<?> clzz = loader.parseClass("activator"); 
				obj = (GroovyObject) clzz.newInstance(); 
				obj.invokeMethod("setUtil", new GroovyScriptUtil(config,loader));// 注入脚本加载工具 
			} catch (Exception e) { 
				e.printStackTrace();
				throw new GroovyActivatorLoadException();
			}
			
			
			ModuleActivator activator = (ModuleActivator) obj; 
			Module module = new Module(activator, config, this);
			
			// 持久化 
			
			
			
			
			modules.put(moduleFile.getName(), module);
			return module;  
		} 
		return null; 
	}
	
	
	
	/**
	 * 卸载
	 */
	public void uninstall(String uuid){
		if(modules.containsKey(uuid)){
			try {
				Module m = this.modules.get(uuid);
				if(m.getStatus() == Module.STATUS_RUNING){
					m.stop();
				}
				this.modules.remove(uuid);
				
				// 本地文件删除
				String moduleDir = WebRealPathHolder.REAL_PATH +"modules"+File.separator+uuid;
				 
				FileTools.deleteDirectory(new  File(moduleDir));  
			} catch (StopModuleActivatorException e) { 
				e.printStackTrace();
			}
		}
		
	
	}


	
	
	public List<Map<String, Object>>  getList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		Set<String> sets = modules.keySet(); 
		for(String uuid : sets){
			Module m = modules.get(uuid); 
			list.add(m.getConfig()); 
		} 
		return list;
	}


	
	
	/**
	 * 获取模块对象
	 * @param uuid
	 * @return
	 */
	public Module getModule(String uuid) {
		return this.modules.get(uuid);
	}
	
}
