/**
 * 
 */
package org.marker.mushroom.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import freemarker.template.TemplateModel;

/**
 * 角色实体
 * 
 * @author jayd
 * @since 1.0
 */
public class Role implements Serializable,TemplateModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -868732181785287519L;
	//角色对象ID
	private long role_id;
	//名称
	private String role_name;
	//描述
	private String descprition;
	//角色与用户的多对多关联的集合
	private Set<User> users;
	//角色与资源多对多关联的集合
	private List<Resource> resources;

	/**
	 * getter方法
	 * 
	 * @return Role对象ID
	 */
	public long getRole_id() {
		return role_id;
	}

	/**
	 * setter方法
	 * 
	 * @param role_id
	 *            Role对象ID
	 */
	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	/**
	 * getter方法
	 * 
	 * @return Role对象name属性
	 */
	public String getRole_name() {
		return role_name;
	}

	/**
	 * setter方法
	 * 
	 * @param role_name
	 *            Role对象name属性
	 */
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	/**
	 * getter方法
	 * 
	 * @return Role对象descprtion属性
	 */
	public String getDescprition() {
		return descprition;
	}

	/**
	 * setter方法
	 * 
	 * @param descprition
	 *            Role对象descprition属性
	 */
	public void setDescprition(String descprition) {
		this.descprition = descprition;
	}

	/**
	 * getter方法
	 * 
	 * @return Role对象关联的user对象的集合
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * setter方法
	 * 
	 * @param users
	 *            Role对象关联的user对象的集合
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * getter方法
	 * 
	 * @return Role对象关联的Resource对象的集合
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * setter方法
	 * 
	 * @param resources
	 *            Role对象关联的Resource对象的集合
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

 
}
