package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;
 

/**
 * URL绝对路径重写标签 作用于主题目录：images、css、js、static文件夹 格式:
 * 
 * 
 * @author marker
 * @date 2013-8-24 下午12:38:13
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class NavigationTagImpl extends Taglib {

	/** 默认构造 */
	public NavigationTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "导航标签");
		config.put("author", "marker");
		config.put("doc", "doc/11.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put(
				
				"(src=|href=|background=)[\"\']((?!http://)(?!/)(?!\\$)(?!\\#).+)[\"\']",
				"$1\"\\${url}/themes/\\${config.themes_path}/$2\"", 0);
	}
 

}
