package org.marker.mushroom.utils;

/**
 * Created by marker on 2018/8/11.
 */

import org.marker.mushroom.core.config.annotation.IgnoreCopyProperties;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * 数组工具类
 *
 *
 * @author marker
 * @create 2018-08-11 09:04
 **/
public class ArrayUtils {


    /**
     * 包含数组里的字符串开头
     * @param jumpUrlPaths
     * @param uri
     * @return
     */
    public static boolean containsStartWith(String[] jumpUrlPaths, String uri) {

        for (String tmp : jumpUrlPaths) {
            if (uri.startsWith(tmp)) {
                return true; // 因为这里是公共文件，所以直接返回了
            }
        }


        return false;
    }


    /**
     * 获取忽略拷贝的字段名称数组
     * @param clzz
     * @return
     */
    public static  String[] getClassIgnoreCopyField(Class clzz){
        List<String> list = new ArrayList<>();
        Field[] fields = clzz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IgnoreCopyProperties.class)) {
                list.add(field.getName());
            }
        }
        Class clzzSuper = clzz.getSuperclass();
        fields = clzzSuper.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IgnoreCopyProperties.class)) {
                list.add(field.getName());
            }
        }
        return list.toArray(new String[]{});
    }


    public static <T> T propertiesToBeanConverter(Properties properties, Class<T> beanClass) {
        try {
            T bean = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if (properties.containsKey(propertyName)) {
                    String value = properties.getProperty(propertyName);
                    if(value == null){
                        continue;
                    }
                    Class<?> propertyType = propertyDescriptor.getPropertyType();
                    if (propertyType == String.class) {
                        propertyDescriptor.getWriteMethod().invoke(bean, value);
                    } else if (propertyType == Integer.class || propertyType == int.class) {
                        propertyDescriptor.getWriteMethod().invoke(bean, Integer.parseInt(value));
                    } else if (propertyType == Boolean.class || propertyType == boolean.class) {
                        propertyDescriptor.getWriteMethod().invoke(bean, Boolean.parseBoolean(value));
                    } else if (propertyType == Long.class || propertyType == long.class) {
                        propertyDescriptor.getWriteMethod().invoke(bean, Long.parseLong(value));
                    } // 可以根据需要添加更多类型的处理
                }
            }
            return bean;
        } catch (IntrospectionException | ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Properties beanToPropertiesConverter (Object bean) {
        Properties properties = new Properties();
        Class<?> clazz = bean.getClass();
        while (clazz!= null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (field.isAnnotationPresent(IgnoreCopyProperties.class)) {
                   continue;
                }
                field.setAccessible(true);
                try {
                    Object value = field.get(bean);
                    if (value!= null) {
                        if (value.getClass().isPrimitive() || value instanceof String || value instanceof Number || value instanceof Boolean) {
                            properties.setProperty(field.getName(), value.toString());
                        } else {
                            Properties subProperties = beanToPropertiesConverter(value);
                            for (String subPropertyName : subProperties.stringPropertyNames()) {
                                properties.setProperty(field.getName() + "." + subPropertyName, subProperties.getProperty(subPropertyName));
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
        return properties;
    }
}
