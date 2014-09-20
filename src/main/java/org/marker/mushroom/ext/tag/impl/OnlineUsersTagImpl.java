package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * 显示统计在线人数滴 调用代码：<!--{online}-->
 * 
 * @author marker
 * */
public class OnlineUsersTagImpl extends Taglib {

	/** 默认构造 */
	public OnlineUsersTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "在线人数");
		config.put("author", "marker");
		config.put("doc", "doc/7.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("<!--\\s*\\{online}\\s*-->",
				"<#if sessions?exists>\\${sessions}<#else>1</#if>", 0);

	}

}
