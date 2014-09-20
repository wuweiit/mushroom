package org.marker.mushroom.core;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;



/**
 * 请求参数解析器
 * 调用get方法可以直接获取到请求参数数据
 * 
 * @author marker
 * */
public final class WebParam {
	
	public static final String FIELD_P    = "p";
	public static final String FIELD_T    = "type";
	public static final String FIELD_ID   = "id";
	public static final String FIELD_PAGE = "page";
	
	
	
	
	/** 当前请求的静态URL名称  */
	public String pageName = "";
	/** 当前请求的模板对象名称 */
	public String template = "";
	/** 内容模型类型 */
	public String moduleType = "";
	/** 内容ID */
	public String contentId = "0";
	/** 页码（默认为1） */
	public String page = "1";//页码
	/** 重定向URL地址 */
	public String redirect;
	
	/** 系统配置信息    */ 
	private static final SystemConfig config = SystemConfig.getInstance();
	
	
	
	/**
	 * 只有通过传递请求对象，才能获取解析数据对象
	 * */
	public static WebParam get(){
		return new WebParam();
	}
	
	
	/**
	 * 私有构造禁止开发者创建此对象
	 * */
	private WebParam(){
		HttpServletRequest req = ActionContext.getReq();
		
		this.pageName   = req.getParameter(FIELD_P);// 页面名称
		this.moduleType = req.getParameter(FIELD_T);// 页面类型
		if(this.moduleType == null){
			this.moduleType = "channel";
		}
		if(!(this.pageName != null && !"".equals(this.pageName))){
			this.pageName = config.get("index_page");// 获取默认主页地址
		}
		this.contentId = req.getParameter(FIELD_ID);// 内容ID
		this.page      = req.getParameter(FIELD_PAGE);// 页码
		// 初始化模版页面（指向错误页面）
		this.template = config.get("error_page");
		req.setAttribute("p", this.pageName);
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("R:\n");
		sb.append("pageName="+pageName+"\n");
		sb.append("moduleType="+moduleType+"\n");
		sb.append("contentId="+contentId+"\n");
		sb.append("page="+page+"\n");
		
		return sb.toString();
	}
	
}
