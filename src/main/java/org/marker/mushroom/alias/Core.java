package org.marker.mushroom.alias;



/**
 * 核心处理对象
 * 
 * @author marker
 * @version 1.0
 */
public interface Core {

	/** freemarker配置对象 */
	String CONFIG_FREEMARKER = "mrcms_freemarker_config";
	
	/** 模板引擎 */
	String ENGINE_TEMPLATE   = "mrcms_template_engine";
	
	
	/** 管理员用户组ID */
	int ADMINI_GROUP_ID = 1;


	/**
	 * 版权注入
	 */
	String COPYRIGHT = "<div style=\"text-align:center;\">Powered by <a name=baidusnap0></a><a href=\"http://cms.yl-blog.com\"><B style='color:black;background-color:#ffff66'>MRCMS</B></a> &copy; 2013-2017 <a href=\"http://cms.yl-blog.com\"><B style='color:black;background-color:#ffff66'>MRCMS</B></a> Inc.</div>\n";


}
