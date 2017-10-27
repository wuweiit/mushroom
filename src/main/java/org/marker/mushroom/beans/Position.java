package org.marker.mushroom.beans;

import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;

/**
 * 行业地址
 *
 * @author marker
 */
@Entity("position")
public class Position implements Serializable{
	private static final long serialVersionUID = 3960328787392429917L;


	private int id;
	private String area; // 地区
	private String company; // 单位
    private String description; // 描述


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
