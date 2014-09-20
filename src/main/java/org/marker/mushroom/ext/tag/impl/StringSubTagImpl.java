package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * 字符串截取标签 格式:
 * 
 * */
public class StringSubTagImpl extends Taglib {

	/** 默认构造 */
	public StringSubTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "字符串截取");
		config.put("author", "marker");
		config.put("doc", "doc/10.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\$\\{\\s*(\\w+\\.?\\w+)\\s+length\\=\\((\\d+)\\)\\s*\\}",
				"<#if ($1?length>$2)>" + "\\${$1[0..$2]}..." + "<#else>"
						+ "\\${$1!}" + "</#if>", 0);
	}

}
