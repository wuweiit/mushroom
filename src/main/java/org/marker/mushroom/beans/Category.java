package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;


@Data
@Entity("category")
public class Category {

	private Integer id = 0;
	private Integer cid;
	private Integer pid;
	private Integer root;
	private String name;
	private String alias;
	private String description;
	private Integer sort;
	private String type;
	private String model;

	/** 模型名称 */
	private String modelName;

}
