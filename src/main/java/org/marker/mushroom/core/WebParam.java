package org.marker.mushroom.core;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;

import javax.servlet.http.HttpServletRequest;


/**
 * 请求参数解析器
 * 调用get方法可以直接获取到请求参数数据
 * 
 * @author marker
 * */
public final class WebParam {
	public static final String ATTR_WEB_PARAM    = ".mrcms.Webparam";

	public static final String FIELD_P    = "p"; // 栏目
	public static final String FIELD_T    = "type";// 类型
	public static final String FIELD_ID   = "id"; // ID
	public static final String FIELD_PAGE = "page";// 页码
	public static final String FIELD_LANG = "lang";// 语言




    public String keywords;


	/**
	 * 语言
	 */
	public String language;

    /** 当前请求的静态URL名称  */
	public String pageName = "";

	/** 当前请求的模板对象名称 */
	public String template = "";

	/** 内容模型类型 */
	public String modelType = "";

	/** 内容ID */
	public Integer contentId = 0;

	/** 页码（默认为1） */
	public String page = "1";//页码contentId

	public int currentPageNo = 1;// 页码

	/** 重定向URL地址 */
	public String redirect;

	/** 页面大小 */
	public int pageSize = 10;

	public String action;

	/**  栏目信息 */
	public Channel channel;
	
	/** 扩展条件查询 */
	public String extendSql;

	/** 表前缀 */
	public String prefix;


    /**
	 * 只有通过传递请求对象，才能获取解析数据对象
	 * */
	public static WebParam get(){
		HttpServletRequest req = ActionContext.getReq();
		WebParam param = (WebParam) req.getAttribute(ATTR_WEB_PARAM);
		if(param != null) return param; // 如果获取到数据，则返回
		return new WebParam(req);
	}
	
	
	/**
	 * 私有构造禁止开发者创建此对象
	 * */
	private WebParam(HttpServletRequest req){
		SystemConfig config = SystemConfig.getInstance();

		this.pageName    = req.getParameter(FIELD_P);// 页面名称
		this.modelType   = req.getParameter(FIELD_T);// 页面类型
		this.language    = req.getParameter(FIELD_LANG);// 语言
        this.page        = req.getParameter(FIELD_PAGE);// 页码
        String contentId = req.getParameter(FIELD_ID);

		if(this.modelType == null){
			this.modelType = "channel";
		}
		if(!(this.pageName != null && !"".equals(this.pageName))){
			this.pageName = config.get("index_page");// 获取默认主页地址
		}

		this.contentId = contentId == null?0: Integer.valueOf(contentId);// 内容ID

		try{
			this.currentPageNo = Integer.parseInt(this.page);
		} catch (Exception e) {}
		// 动作
		this.action = req.getParameter("action");
		if(StringUtils.isEmpty(action)){
			this.action = "page";
		}
		this.keywords = req.getParameter("keywords");

		if(StringUtils.isBlank(this.language)){
            this.language = (String) req.getAttribute(AppStatic.WEB_APP_LANG);
        }

		// 初始化模版页面（指向错误页面）
		this.template = config.get("error_page");
		req.setAttribute("p", this.pageName);
		
		// 绑定请求数据到请求对象，避免二次解析
		req.setAttribute(ATTR_WEB_PARAM, this);
		
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("R:\n");
		sb.append("pageName=" + pageName + "\n");
		sb.append("modelType=" + modelType + "\n");
		sb.append("contentId=" + contentId + "\n");
		sb.append("page=" + page + "\n");
		sb.append("lang=" + language + "\n");

		return sb.toString();
	}


	/**
	 * 是否存在内容Id
	 * @return
	 */
	public boolean existsContentId() {
		return this.contentId != null && this.contentId != 0;
	}
}
