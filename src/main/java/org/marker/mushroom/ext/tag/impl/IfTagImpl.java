package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * if判断标签 格式: <!--{if:${channel.id != current.id}}-->
 * 
 * <!--{else}-->
 * 
 * <!--{/if}-->
 * 
 * */
public class IfTagImpl extends Taglib{

	/** 默认构造 */
	public IfTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "逻辑判断");
		config.put("author", "marker");
		config.put("doc", "doc/4.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put(
				"\\s*<!--\\s*\\{if:[\\x20]*\\$\\{(.*)\\}[\\x20]*\\}\\s*-->[\\x20]*\\n?",
				"<#if ($1) >", 0);
		this.put("\\s*<!--\\s*\\{else\\}\\s*-->[\\x20]*\\n?", "<#else>", 0);
		this.put("\\s*<!--\\s*\\{/if\\}\\s*-->[\\x20]*\\n?", "</#if>\n", 0);
	}

}
