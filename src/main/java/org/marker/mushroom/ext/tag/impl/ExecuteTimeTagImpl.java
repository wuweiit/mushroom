package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * 执行时间标签 调用格式: <!--{executetime}--> 单位为秒
 * 
 * 已更新为Freemarker
 * 
 * @author marker
 * */
public class ExecuteTimeTagImpl extends Taglib{

	/** 默认构造 */
	public ExecuteTimeTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "执行时间");
		config.put("author", "marker");
		config.put("doc", "doc/2.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\s*<!--\\s*\\{executetime\\}\\s*-->",
				"\\${(.now?long-_starttime)/1000} ", 0);
	}

}
