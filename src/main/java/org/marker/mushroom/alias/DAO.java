package org.marker.mushroom.alias;



/**
 * 
 * 数据库操作对象别名接口
 * 
 * 博客：www.yl-blog.com
 * 微博：t.qq.com/wuweiit
 * 
 * @author marker
 */
public interface DAO {

	String MENU   = "mrcms_menu_dao";
	
	String COMMON = "commonDao";
	
	String STATISTICS = "mrcms_statistics_dao";
	
	
	/** 内容模型  */
	String MODULE = "moduleDao";
	
	/** 内容模型日志DaoBean */
	String MODULE_LOG = "moduleLogDao";
	
	/** 栏目DaoBean */
	String CHANNEL = "channelDao";

	/** 插件DaoBean */
	String PLUGIN = "pluginDao";

	/** 用户登录日志DaoBean */
	String USER_LOGIN = "userLoginLogDao";
	
	/** 权限 DaoBean */
	String PERMISSION = "permissionDao";
	
	/** 文章 DaoBean */
	String ARTICLE = "articleDao";


	/** 文章 DaoBean */
	String Thematic = "thematicDao";


	/** 文章 DaoBean */
	String ARTICLE_Taolu = "articleTaoluDao";
	/** 模型 DaoBean */
	String MODEL = "modelDao";
	
	/** 分类 DaoBean */
	String CATEGORY = "categoryDao";


    /** 分类 DaoBean */
    String SLIDE = "slideDao";

    String CONTENT = "contentDao";
	String LINK = "linkDao";
    String DOCTOR = "doctorDao";
	String DOCTORGH = "doctorGhDao";
	String UserGroupChannelDao = "UserGroupChannelDao";
	String UserGroupCategoryDao = "UserGroupCategoryDao";
}
