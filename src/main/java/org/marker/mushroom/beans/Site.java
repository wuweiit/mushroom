package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;



@Data
@Entity("site")
public class Site implements Serializable{

	private Integer id = 0;
	private String title;
	private String host;
	private String theme;
	private String describe;

}
