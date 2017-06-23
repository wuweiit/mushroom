package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 幻灯片
 *
 * @author marker
 * Created by marker on 17/6/8.
 */
@Data
@Entity("slide")
public class Slide implements Serializable{

    /** ID */
    private Integer id;
    // 名称
    private String name;
    // 宽度
    private int width;
    // 高度
    private int height;
    // 描述
    private String description;
    // 创建时间
    private Date createTime;



}
