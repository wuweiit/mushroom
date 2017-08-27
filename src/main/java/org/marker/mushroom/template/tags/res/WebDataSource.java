/**
 * 
 */
package org.marker.mushroom.template.tags.res;

import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.springframework.util.StringUtils;

/**
 *
 * 数据源定制
 *
 *
 * @author marker
 * @date 2013-9-14 下午6:00:15
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */ 
public abstract class WebDataSource {

	/** 遍历对象名称 */
	protected String var = "it";

	/** 数据结构类型，list：列表，page:分页 */
    protected String type = "list";

	/** 表前缀（如果等于null使用系统自带的mr_前缀） */
    protected String prefix = null;

	//request传递的集合名称
	protected String items;

	// SQL 查询字符串
	protected String queryString;

	// whereIn数据
	protected String whereIn;
	
	//因为这个是在程序启动后，才有生成这类对象
	protected ContentModelContext contentModelContext;
	
	public WebDataSource() {
		// 初始化模型工厂
	    if(contentModelContext == null){
	    	contentModelContext =  ContentModelContext.getInstance(); 
		} 
	}
	
	/**
	 * 设置var和items
	 * @param var
	 * @param items
	 */
	public void setVarAndItems(String var, String items) {
		this.var = var;
		this.items = items;
	}
	
	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}
	
	
	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * @return the items
	 */
	public String getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(String items) {
		this.items = items;
	}
 
	/**
	 * 此方法右模板引擎调用，一般来说只调用一次
	 * 功能：将从模板引擎中收集的信息来生成sql语句
	 * @throws SystemException 
	 */
	public abstract void generateSql() throws SystemException;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWhereIn() {
        return whereIn;
    }

    public void setWhereIn(String whereIn) {
        this.whereIn = whereIn;
    }


    /**
     * 获取插件扩展表前缀
     *
     * @return String
     */
    public String getPrefix() {
		return prefix;
	}


    /**
     * 获取系统表前缀。
     *
     * @return String
     */
	public String getSystemPrefix(){
        return  DataBaseConfig.getInstance().getPrefix();
    }



	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}