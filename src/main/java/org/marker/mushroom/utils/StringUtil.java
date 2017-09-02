package org.marker.mushroom.utils;/**
 * Created by marker on 2017/9/2.
 */

/**
 * @author marker
 * @create 2017-09-02 下午3:24
 **/
public class StringUtil {

    /**
     * 构造字符串
     * @param space
     * @param count
     * @return
     */
    public static String buidString(String space, int count){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < count; i++) {
            sb.append(space);
        }
        return sb.toString();
    }


}
