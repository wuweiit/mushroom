package org.marker.mushroom.ext.tag.impl;

import org.marker.mushroom.ext.tag.AbstractTag

/**
 * 时间格式标签 调用格式: ${dsad format=(yy-DD-MM)}
 * 
 * 
 * @author marker
 * */
public class TagImpl extends AbstractTag{
	
	 /** 默认构造 */
	public TagImpl() {
		// 配置模型（必须调用）
		configure([
			name  : "日期格式  ",
			author: "marker",
			doc : "doc/2.md",
			description : '系统内置'
	    ]);
		 
		 
		this.put("\${s*([a-z.]+)s+format=(([a-z A-Z:-]+))s*}",
				
				/<#if $1?exists>\$\{$1?string("$2")}<\/#if>/, 0);

	}

}
