package org.marker.mushroom.beans;

import java.io.Serializable;

import org.marker.mushroom.dao.annotation.Entity;

/**
 * 插件Bean
 * @author marker
 * */
@Entity("plugin")
public class Plugin implements Serializable{
	private static final long serialVersionUID = -4808038290887993182L;

	/** ID */
	private Integer id;
	/** 插件名称 */
	private String name;
	/** 类路径 */
	private String uri;
	/** 状态 */
	private int status;
	/** 标签 */
	private String mark;
	/** 作者 */
	private String author;
	/** 版本 */
	private String ver;
	/** 描述 */
	private String description;
	
	
	
	
 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
 
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
