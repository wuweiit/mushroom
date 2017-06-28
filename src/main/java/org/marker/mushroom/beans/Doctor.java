package org.marker.mushroom.beans;

import lombok.Data;
import org.marker.mushroom.dao.annotation.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 医生信息
 * @author marker
 * Created by marker on 2017/6/13.
 */

@Entity("doctor")
@Data
public class Doctor implements Serializable {

    /** ID */
    private Integer id;
    /** 所属栏目ID */
    private Integer cid;
    /** 所属分类（科室）ID */
    private Integer did;
    /** 图标 */
    private String icon;
    /** 医生名称 */
    private String name;
    /** 关键字 */
    private String keywords;
    /** 科室  */
    private String deptname;
    /** 职称 */
    private String jobname;
    /** 类型医生 */
    private String typeName;
    /** 描述 */
    private String description;
    /** 擅长 */
    private String goodAt;



    /** 详细描述 */
    private String content;

    private Date time;

    /** 更新时间 */
    private Date updateTime;

    private int views;

    private int status;
}
