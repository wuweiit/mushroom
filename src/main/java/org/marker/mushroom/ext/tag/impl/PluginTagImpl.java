package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 *
 *
 *
 * 插件调用标签 <!--{pulgin name=(comment) invoke=(adsads)}-->
 * */
public class PluginTagImpl extends Taglib {

	/** 默认构造 */
	public PluginTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "插件调用");
		config.put("author", "marker");
		config.put("doc", "doc/8.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put(
				"<!--\\s*\\{\\s*plugin\\s*name=\\((\\w+)\\)\\s*invoke=\\((\\w+)\\)\\s*\\}\\s*-->",
				"<@Plugin pluginName=\"$1\" invoke=\"$2\"></@Plugin>",
				0);

	}

}
