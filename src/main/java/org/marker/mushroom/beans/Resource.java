/**
 * 
 */
package org.marker.mushroom.beans;

import java.util.List;
 
/**
 * 与角色关联的资源实体
 * 
 * @author jayd
 * @since 1.0
 */
public class Resource implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4160433497595897081L;

	/**
	 * resourceType常量
	 */
	public static final String TYPE_MENU_ONE = "menu_one";
	public static final String TYPE_MENU_TWO = "menu_two";
	public static final String _TYPE = "menu";
	
	//资源对象ID
	private Integer id;
	//名称
	private String name;
	//资源类型
	private String type;
	//资源的字符串，如为URL类型的就是URL地址字符串
	private String res_string;
	//描述
	private String description;
	//资源多对对关联的角色集合
	private List<Role> roles;
	
 

	/**
	 * getter方法
	 * 
	 * @return Resource对象的name属性
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter方法
	 * 
	 * @param name
	 *            Resource对象的name属性
	 */
	public void setName(String name) {
		this.name = name;
	}
 

 
	/**
	 * getter方法
	 * 
	 * @return Resource对象的res_string属性
	 */
	public String getRes_string() {
		return res_string;
	}

	/**
	 * setter方法
	 * 
	 * @param res_string
	 *            Resource对象的res_string属性
	 */
	public void setRes_string(String res_string) {
		this.res_string = res_string;
	}

	/**
	 * getter方法
	 * 
	 * @return Resource对象的descrption属性
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * setter方法
	 * 
	 * @param description
	 *            description属性
	 */
	public void setDescrption(String description) {
		this.description = description;
	}

	/**
	 * getter方法
	 * 
	 * @return Resource对象的关联的role对象集合
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * setter方法
	 * 
	 * @param roles
	 *            Resource对象的关联的role对象集合
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

//	/**
//	 * 利用org.apache.commons.lang.builder.ToStringBuilder重写了toString方法。
//	 */
//	@Override
//	public String toString() {
//		return new ToStringBuilder(this).append("resource_id", resource_id).append("name", name).append("res_type",
//				 type).append("res_string", res_string).append("description", description).toString();
//	}

//	/**
//	 * 利用org.apache.commons.lang.builder.HashCodeBuilder重写了hashCode方法。
//	 */
//	@Override
//	public int hashCode() {
//		return new HashCodeBuilder().append(resource_id).append(name).append( type).append(res_string).append(
//				description).toHashCode();
//	}

 

//	/**
//	 * 可访问该资源的授权名称字符串, 多个授权用','分隔.
//	 */
//	public String getAuthNames() {
//		return ReflectionUtils.convertElementPropertyToString(roles, "role_name", ",");
//	}
}
