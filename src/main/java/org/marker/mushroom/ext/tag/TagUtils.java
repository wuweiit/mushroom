package org.marker.mushroom.ext.tag;

import java.util.UUID;

/**
 * 模板标签工具类
 *
 * @author marker
 *
 * Created by marker on 17/5/21.
 */
public class TagUtils {


    /**
     * 获取UUID字符串
     * （32位）
     * @return
     */
    public static Object getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
