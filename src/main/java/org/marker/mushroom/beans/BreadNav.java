package org.marker.mushroom.beans;

import lombok.Data;

import java.io.Serializable;

/**
 * 面包屑导航数据模型
 *
 * @author marker
 * Created by marker on 2017/6/12.
 */
@Data
public class BreadNav implements Serializable {

    public String name;

    private String url;
}
