package org.marker.mushroom.beans;

import java.io.Serializable;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;
import org.marker.mushroom.dao.annotation.EntityFieldIgnore;

/**
 * 栏目实体
 * @author marker
 * */
@Entity("channel")
@Data
public class Channel implements Serializable{
	private static final long serialVersionUID = -7383542815506431998L;
	
	/** 自动生成ID */
	private int id = 0;
	/** 上级栏目ID */
	private int pid = 0;
	/** 图标 */
	private String icon;
	/** 栏目名称 */
	private String name;
	/** 语言键 */
	private String langkey;
	/** 栏目关键字 */
	private String keywords;
	/** 栏目描述 */
	private String description;
	/** URL地址 */
	private String url;
	/** 模板 */
	private String template;
	/** 排序 */
	private int sort;
	/** 是否结束 */
	private int end = 0;

	/** 条数 */
	private int rows;
	/** 是否隐藏 */
	private short hide; 
	/** 重定向URL*/
	private String redirect;

	/** 分类Id 列表 */
	private String categoryIds;

	/** 文章内容 */
	@EntityFieldIgnore
	private String content;

	/**
	 * 文章内容Id
	 * */
	private int contentId;
	

}
