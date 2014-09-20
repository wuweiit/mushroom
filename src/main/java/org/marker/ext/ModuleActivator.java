package org.marker.ext;

import org.marker.ext.exception.StartModuleActivatorException;
import org.marker.ext.exception.StopModuleActivatorException;
import org.marker.ext.module.ModuleContext;




/**
 * 模块启动器
 * @author marker
 * @version 1.0
 */
public interface ModuleActivator {

 
	/**
	 * 启用
	 * @param context
	 * @throws Exception
	 */
	public void start(ModuleContext context) throws StartModuleActivatorException;
	
	
	/**
	 * 停止
	 * @param context
	 * @throws Exception
	 */
	public void stop(ModuleContext context) throws StopModuleActivatorException;
}
