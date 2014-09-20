package org.marker.mushroom.core.exception;


/**
 * @author marker
 * @date 2013-8-24 上午11:50:25
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SystemException extends Exception { 
	
	//异常信息
	private String errMessage;
	
	
	
	
	private static final long serialVersionUID = -2832868028518271545L;

	
	
	/**
	 * 
	 */
	public SystemException(String message) {
		this.errMessage = message;
	} 
	
	
	
	
	public String renderHTML(){
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>系统错误提示</title>");
		sb.append("<style type=\"text/css\">.msg-box{border: 1px solid red; margin: 20px auto; width: 800px;}.msg-box-inner{border:1px solid #fff; padding:15px; background:#f0f6f9;}.msg-title{border-bottom:1px #9CC solid; font-size:26px;font-family: \"Microsoft Yahei\", Verdana, arial, sans-serif; line-height:40px; height:40px; font-weight:bold}.msg-line{height:20px; border-top:1px solid #fff}.msg-item{border:1px dotted #F90; border-left:6px solid #F60; padding:15px; background:#FFC}.msg-button{ font-size:15px; margin-top:20px;}</style>");
		sb.append("</head><body>");
		sb.append("<div class=\"msg-box\">");
		sb.append("<div class=\"msg-box-inner\">");
		sb.append("<div class=\"msg-title\">系统错误提示:</div>");
		sb.append("<div class=\"msg-line\"></div>");
		sb.append("<div class=\"msg-item\"> 出错信息： "+errMessage+"</div>"); 
		sb.append("<div class=\"msg-button\">请您选择 &nbsp;&nbsp;");
		sb.append("<a href=\"javascript:window.location.reload();\">重试</a> &nbsp;&nbsp;");
		sb.append("<a href=\"javascript:history.back()\">返回</a>  或者  &nbsp;&nbsp;");
		sb.append("<a href=\"/\">回到首页</a>");
		sb.append("</div></div></div></body></html>");
		return sb.toString();
	}




	/**
	 * @return the errMessage
	 */
	public String getErrMessage() {
		return errMessage;
	}




	/**
	 * @param errMessage the errMessage to set
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
	
	
}
