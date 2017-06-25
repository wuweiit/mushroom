package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

 



/**
 * 文章对象
 * @author marker
 * */
@Data
@Entity("article")
public class Article implements Serializable {
	private static final long serialVersionUID = -2456959238880328330L;
	
	/** 文章ID */
	private Integer id;
	/** 所属栏目ID */
	private Integer cid;
	/** 所属分类ID */
	private Integer did;
	/** 图标 */
	private String icon;
	/** 标题 */
	private String title;
	/** 关键字 */
	private String keywords;
	/** 描述 */
	private String description;
	/** 作者 */
	private String author;
	/** 浏览量 */
	private int views;
	/** 内容 */
	private String content;
	/** 创建时间 */
	private Date time;
	/** 更新时间 */
	private Date updateTime;
	
	/* 发布状态：0草稿 1发布*/
	private int status;
	
	/** 引用地址 */
	private String source;
	
	/** 内容类型 */
	private int type;
	
	/** 原始内容 */
	private String orginal;

 
 

	
	
 
}
