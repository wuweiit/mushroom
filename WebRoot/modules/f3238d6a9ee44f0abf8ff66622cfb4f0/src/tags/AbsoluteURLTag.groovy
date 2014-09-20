package org.marker.mushroom.ext.tag.impl;


import org.marker.mushroom.ext.tag.AbstractTag;


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
public class TagImpl extends AbstractTag {

	/** 默认构造 */
	public TagImpl() {
		// 配置模型（必须调用）
		configure([
			name  : "绝对路径处理",
			author: "marker",
			doc : "doc/1.md",
			description : '系统内置'
	    ]);
	 
		put("(src=|href=|background=)[\"\']((?!http://)(?!/)(?!\$)(?!\\#).+)[\"\']",
		    /$1\$\{url}\/themes\/\$\{config.themes_path}\/$2/, 0);
	}

}
