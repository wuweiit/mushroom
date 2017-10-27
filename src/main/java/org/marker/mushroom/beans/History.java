package org.marker.mushroom.beans;

import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;

/**
 * 发展历程
 *
 * @author marker
 */
@Entity("history")
public class History implements Serializable{
	private static final long serialVersionUID = 3960328787392429917L;


	private int id;
	private int year; // 年
	private String title; // 说明
    private String description; // 描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
