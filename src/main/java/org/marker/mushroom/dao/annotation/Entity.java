package org.marker.mushroom.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * 数据库实体信息注解
 * 表名称是必须的，默认主键名称是id
 * @author marker
 * */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.TYPE})//方法注解
public @interface Entity {
	String value();//名称
	String key() default "id";//名称
}
