package org.marker.mushroom.core;


/**
 * 存放一些变量名称方便管理
 * @author marker
 * */
public interface AppStatic {
	
	// 真实IP存放请求名称
	String REAL_IP = "realip";

	
	
	

	/** WEB应用安装状态 */
	String WEB_APP_INSTALL = "website_install";
	/** WEB应用配置变量名 */
	public static String WEB_APP_CONFIG  = "config";
	/** WEB异常或错误变量名 */
	public static final String WEB_APP_ERROR   = "error";
	/** WEB错误代码变量名 */
	public static String WEB_APP_ERROR_CODE = "err_code";
	/** WEB模板当前栏目变量名 */
	public static final String WEB_CURRENT_CHANNEL = "current";
	/** WEB URL变量名 */
	public static final String WEB_APP_URL = "url";
	/** 语言 */
	public static final String WEB_APP_LANG = "lang";
	/** WEB主题变量名 */
	public static final String WEB_APP_THEME_URL = "themeurl";
	
	/** WEB会话变量名 */
	public static final String WEB_APP_SESSION_ADMIN = "admin";
	/** WEB会话变量名 */
	public static final String WEB_APP_SESSION_USER = "user";
	/** WEB后台已登录变量名 */
	public static final String WEB_APP_SESSSION_LOGINNAME = "loginusername";
	/** WEB后台登陆用户组ID */
	public static final String WEB_APP_SESSSION_USER_GROUP_ID = "loginusergroupid";
	
	
	
	
	
	/** WEB验证码变量名 */
	public static String WEB_APP_AUTH_CODE = "randauthcode";
	/** WEB碎片变量名 */
	public static String WEB_APP_CHIP = "chip";
	
	/** WEB开始执行时间变量名 */
	public static String WEB_APP_STARTTIME = "_starttime";
	
	/**  */
	public static String JSON_OBJECT_NAME = "data";
	
	
	public static final String WEB_APP_PAGE = "page"; 
}
