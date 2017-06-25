package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 内容数据
 * 通过统一的表来管理内容
 *
 *
 *
 * @author marker
 * */
@Entity("content")
@Data
public class Content implements Serializable {
	private static final long serialVersionUID = -2456959238880328330L;
	
	/** 文章ID */
	private Integer id;
	/** 数据模型 */
	private String model;
	/** 标题 */
	private String content;
	/** 关键字 */
	private Date time = new Date();

 

	
 
}
