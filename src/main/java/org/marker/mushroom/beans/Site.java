package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;


/**
 * 站点信息
 * @author marker
 */
@Data
@Entity("site")
public class Site implements Serializable{

	/**
	 * 站点id
	 */
	private Integer id = 0;

	/**
	 * 站点标题
	 */
	private String title;

	/**
	 * 站点关键字
	 */
	private String keywords;

	/**
	 * 站点域名
	 */
	private String host;

	/**
	 * 站点描述
	 */
	private String theme;

	/**
	 * 站点描述
	 */
	private String describe;

}
