package org.marker.mushroom.beans;

import java.io.Serializable;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;



@Data
@Entity("link")
public class Link implements Serializable{
	private static final long serialVersionUID = 3960328787392429917L;


	private int id;
	private String name;
	private String url;
	private String description;
	private String icon;
	private int views;

}
