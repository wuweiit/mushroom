/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.ext.model;
 
 
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.template.tags.res.SqlDataSource;

/**
 * 模型驱动引擎
 * 
 * @author marker
 * @date 2013-9-18 上午11:37:02
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public interface IContentModelParse{
	
	public static final int INSTALL_FAILD = 00;
	// 已经存在
	public static final int INSTALL_EXIST = 01;
	
	public static final int INSTALL_SUCCESS = 02;
	
	
	/** 解析错误 */
	public static final int STATUS_ERROR = 0;
	
	/** 重定向 */
	public static final int STATUS_REDIRECT = 1;
	
	/** 内容模型 */
	public static final int STATUS_MODULE = 2;
	/** 内容模型未找到 */
	public static final int STATUS_NOT_FOUND_MODULE = 3;
	
	
	
	/**
	 * 通过解析对象找到对应的模型
	 * @param resv
	 * @return
	 * @throws SystemException 
	 */
	public int parse(WebParam param) throws SystemException;
	
	
	
	public StringBuilder parse(String tableName, SqlDataSource sqldatasource) throws SystemException;
	
	
	
	
	
}
