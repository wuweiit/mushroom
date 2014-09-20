package org.marker.mushroom.alias;


/**
 * SLF4J 日志别名
 * 
 * @author marker
 * @version 1.0
 */
public interface LOG {

	/** 名称与日志配置文件对应，因此不能修改其值 */
	String DAOENGINE = "daoEngine";
	
	/** 业务日志别名 */
	String SERVICE_LAYER = "serviceLayer";

	/** Web前台日志接口 */
	String WEBFOREGROUND = "foreground";
	
	/** 模板引擎日志接口 */
	String TEMPLATE_ENGINE = "templatengine";
}
