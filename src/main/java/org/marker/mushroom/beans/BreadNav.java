package org.marker.mushroom.beans;


import java.io.Serializable;

/**
 * 面包屑导航数据模型
 *
 * @author marker
 * Created by marker on 2017/6/12.
 */
public class BreadNav implements Serializable {

    public String name;

    private String url;


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
}
