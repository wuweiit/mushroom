package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.ext.tag.Taglib;

/**
 * URL重写标签 格式:
 * @author marker
 * @date 2013-8-24 下午12:47:32
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class URLRewriteTagImpl extends Taglib {
	/** 默认构造 */
	public URLRewriteTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "URL重写");
		config.put("author", "marker");
		config.put("doc", "doc/11.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		// \\\\{?\\w+\\}?\\.?\\w*\\??[\\w+\\-?\\=?\\$?\\{?\\w+\\.\\}?&?]*
		this.put("href\\=[\"\']\\$\\{(.+\\..+[^!])\\}[\'\"]",
				"href=\"\\${url}\\${encoder($1)}\"", 0);

	}

}
