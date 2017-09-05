package org.marker.mushroom.beans;

import java.io.Serializable;

import org.marker.mushroom.dao.annotation.Entity;



@Entity("link")
public class Link implements Serializable{
	private static final long serialVersionUID = 3960328787392429917L;


	private int id;
	private String name;
	private String url;
	private String description;
	private String icon;
	private int views;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}
}
