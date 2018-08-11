package org.marker.mushroom.utils;

/**
 * Created by marker on 2018/8/11.
 */

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
}
