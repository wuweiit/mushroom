package org.marker.mushroom.alias;
 

/**
 * 目前包含一级菜单类型，与数据库中的数据保持同步。
 * 
 * 注意：不能修改数据库中菜单的type字段
 * 
 * @author marker
 * @version 1.0
 */
public interface MenuType {

	/* 首页 */
	String L1_HOME    = "b8db3088786a43e2916b810bbab8425b";
	
	/* 系统 */
	String L1_SYSTEM  = "a1d6ac255acf4feab32ee70795b7b265";
	
	/* 栏目 */
	String L1_CHANNEL = "91ea1c25538b4c4e90401289cbe981fb";
	
	/* 内容 */
	String L1_CONTENT = "bb2bc5fb802544a2a4634013d2c19936";
	
	/* 用户 */
	String L1_USER    = "c18166b148444928a4bab628da4190c8"; 
	
	/* 扩展 */
	String L1_EXTEND  = "bf41a349eec94e039648cc6de833440a";
	
	/* 商店 */
	String L1_STORE   = "ed3fc978ab86454ca88166a7e40a46a5";
	
	
}
