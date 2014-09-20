package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.Date;

import org.marker.mushroom.dao.annotation.Entity;


/**
 * 内容模型
 * @author marker
 * */
@Entity("model")
public class Model implements Serializable{
	private static final long serialVersionUID = -4259189870337220290L;
	
	/** ID自动生成 */
	private Integer id;
	/** 图标 */
	private String icon;
	/** 内容模型中文名称 */
	private String name;
	/** 内容模板 */
	private String template;
	/** 模型类型 */
	private String type;
	/** 作者 */
	private String author;
	/** 版本 */
	private String version;
	/** 安装时间 */
	private Date time;
	/** 模型描述 */
	private String description; 
	
	private String module;
	public Model(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
 

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
 
	 
	
	
 
	 
}
