package org.marker.mushroom.ext.model.impl;
 
import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.template.tags.res.WebDataSource;




/**
 * 商品模型处理
 * 
 * @author marker
 * */ 
public class GoodsModelImpl extends ContentModel {
	
	
	public GoodsModelImpl(){ 
		Map<String,Object> config = new HashMap<String,Object>();
		config.put("icon", "images/demo.jpg");
		config.put("name", "商品模型");
		config.put("author", "marker");
		config.put("version", "0.1");
		config.put("type", "goods");
		config.put("template", "goods.html");
		config.put("description", "系统内置模型");
		configure(config);
	}
	
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, WebDataSource WebDataSource) {
//		String prefix = getPrefix();// 表前缀，如："yl_"
//		StringBuilder sql = new StringBuilder();
//		sql.append("select A.*, concat('p=',A.url) 'url' from ");
//		sql.append(prefix).append("channel ").append(Sql.QUERY_FOR_ALIAS);
		return new StringBuilder();
	}
	
	

	public Page doPage(WebParam param) { 
		return null;
	}
	
	
}
