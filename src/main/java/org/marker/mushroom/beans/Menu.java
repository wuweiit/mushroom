package org.marker.mushroom.beans;

import java.io.Serializable;

import org.marker.mushroom.dao.annotation.Entity;


@Entity("user_menu")
public class Menu implements Serializable {

	private static final long serialVersionUID = 8538161497615816793L;

	/** ID */
	private Integer id;
	/** 上级ID */
	private Integer pid;
	private String icon;
	private String name;
	private String url;
	/** UUID唯一码 */
	private String type;
	private int sort = 0;
	private String description;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
 
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
