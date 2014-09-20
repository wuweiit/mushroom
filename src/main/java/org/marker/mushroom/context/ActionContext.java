package org.marker.mushroom.context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 保存各种作用域，此类设计采用ThreadLocal模式并发处理。
 * 
 * @author marker
 * */
public final class ActionContext {

	
	
	/** 全局对象 */
	private volatile static ServletContext application;

	
	/**
	 * 目前放在SpringAware中进行绑定的
	 * @param application2
	 * @see{org.marker.mushroom.holder.MushRoomInitBuildHolder}
	 */
	public static final void currentThreadBindServletContext(
			ServletContext application2) {
		if (ActionContext.application == null) {
			ActionContext.application = application2;
		}
	}
	
	
	/**
	 * 绑定当前Request和Response对象到当前线程
	 * 增加同步解决并发问题
	 * @param request
	 * @param response
	 */
	public synchronized static final void currentThreadBindRequestAndResponse(
			HttpServletRequest request, HttpServletResponse response) {
		ActionScopeData scopeData = ActionScopeData.getInstance();
		scopeData.setRequest(request);
		scopeData.setResponse(response);
	}

	public static HttpServletRequest getReq() {
		return ActionScopeData.getInstance().getRequest();
	}

	public static HttpServletResponse getResp() {
		return ActionScopeData.getInstance().getResponse();
	}

	public static final ServletContext getApplication() {
		return application;
	}

	public static final void remove() {
		ActionScopeData.threadLocalData.remove();
	}

}

/**
 * 
 * */
class ActionScopeData {

	private HttpServletRequest request;
	private HttpServletResponse response;
	static final ThreadLocal<ActionScopeData> threadLocalData = new ThreadLocal<ActionScopeData>();

	public static ActionScopeData getInstance() {
		ActionScopeData instance = threadLocalData.get();
		if (instance == null) {
			threadLocalData.set(instance = new ActionScopeData());
		}
		return instance;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}