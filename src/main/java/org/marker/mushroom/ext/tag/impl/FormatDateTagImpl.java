package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * 时间格式标签 调用格式: ${dsad format=(yy-DD-MM)}
 * 
 * 
 * @author marker
 * */
public class FormatDateTagImpl extends Taglib{
	
	
	/** 默认构造 */
	public FormatDateTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "格式化日期");
		config.put("author", "marker");
		config.put("doc", "doc/3.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\$\\{\\s*([a-z.]+)\\s+format\\=\\(([a-z A-Z:-]+)\\)\\s*\\}",
				"<\\#if $1?exists>\\${$1?string(\"$2\")}</\\#if>", 0);

	}

}
