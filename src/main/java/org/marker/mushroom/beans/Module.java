package org.marker.mushroom.beans;

import java.io.Serializable;

import org.marker.mushroom.dao.annotation.Entity;


/**
 * 内容模型
 * @author marker
 * */
@Entity("module")
public class Module implements Serializable{
	private static final long serialVersionUID = -4259189870337220290L;
	
	/** ID自动生成 */
	private long id;
	/** 内容模型中文名称 */
	private String name;
	/** 内容模板 */
	private String template;
	/** 模型类型 */
	private String type;
	/** 类路径 */
	private String uri;
	/** 作者 */
	private String author;
	/** 版本 */
	private int version;
	
	private String module;
	
	public Module(){}
	
	
	
	
	public Module(String name, String author, String type, String template,
			String uri, int version) { 
		this.name = name;
		this.author = author;
		this.type = type;
		this.template = template;
		this.uri = uri;
		this.version = version;
	}




	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}




	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}




	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}




	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}




	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}




	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}




	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}




	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}




	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}




	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}




	public String getModule() {
		return module;
	}




	public void setModule(String module) {
		this.module = module;
	}




	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
 
	 
}
