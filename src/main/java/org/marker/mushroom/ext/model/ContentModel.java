package org.marker.mushroom.ext.model;

import java.io.Serializable;
import java.util.Map;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.urlrewrite.URLRewriteEngine;



/**
 * 抽象模型
 * 模型是拿来匹配和我发布文章或者是产品信息的时候用的，
 * 如果我发布一个商品，那么就会调用商品模型，这样区分出来
 *  
 * 
 * @author marker
 * 
 * */
public abstract class ContentModel{
	
	
	public static final String CONFIG_TYPE = "type";
	public static final String CONFIG_MODULE = "module";




	// URL 重写引擎
	public URLRewriteEngine urlrewrite = SingletonProxyFrontURLRewrite.getInstance();
	
	/** 数据库模型引擎 */
	public ISupportDao commonDao;


    protected DataBaseConfig dbconfig = DataBaseConfig.getInstance();
	
	/** 内容模型配置信息 */
	protected Map<String,Object> config;
	
	
	
	/**
	 * 初始化一些必要工具
	 */
	public ContentModel() {
		this.commonDao = SpringContextHolder.getBean(DAO.COMMON);
	}
	
	
	
	/**
	 * 配置模型参数
	 * @param config map
	 */
	public void configure(Map<String, Object> config){
		this.config= config;
	}
	
	
	 
 
	
	
	public Object get(String key){
		return config.get(key);
	}
	
	
	/**
	 * 设置模型的模块UUID
	 * @return
	 */
	public Object setModule(Object uuid){
		return config.put(CONFIG_MODULE, uuid);
	}
	
	
	
	
	/**
	 * 获取数据库前缀
	 * @return
	 */
	public String getPrefix(){
		DataBaseConfig dbconfig = DataBaseConfig.getInstance();
		return dbconfig.getPrefix();
	}


    /**
     * 获取模型表前缀
     * @return
     */
	public String getModelPrefix(){
	    return (String) this.config.get("prefix");
    }




	public Map<String, Object> getConfig() {
		return config;
	}


	
	/**
	 * 分类单级数据查询
	 * */
	public void doSingleCategory(){
		
	}
	
	

	/**
	 * 处理分页查询信息
	 * */
	public Page doPage(WebParam param){
		return null;
	}


	public void doPage(Channel current, WebParam param){}

	/**
	 * Web前端生成SQL语句(模板引擎会调用来生成sql语句)
	 * */
	public StringBuilder doWebFront(String tableName, WebDataSource WebDataSource){
		return null;
	}


	
	/**
	 * 备份数据
	 */
	public void backup(){
		
	}
	
	
	/**
	 * 恢复数据
	 */
	public void recover(){
		
	}


	
	/**
	 * 抓取内容
	 * 可能有的模型不需要获取内容，由此类提供适配。
	 * @param cid 内容ID
	 * @return
	 */
	public void fetchContent(Serializable cid){ }
	
	

	
}
