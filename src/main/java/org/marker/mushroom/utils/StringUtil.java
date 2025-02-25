package org.marker.mushroom.utils;/**
 * Created by marker on 2017/9/2.
 */

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    /**
     * 拆分为int集合
     * @param ids id集合
     * @param charStr 分隔符
     * @return
     */
    public static List<Integer> splitInt(String ids, String charStr) {
        if (Objects.isNull(ids)) {
            return Collections.emptyList();
        }
        List<Integer> list = new ArrayList<>();
        String[] strList = ids.split(charStr);
        for(String tmp : strList){
            if("".equals(tmp)){
                continue;
            }
            list.add(Integer.parseInt(tmp));
        }
        return list;
	}


    /**
     * 拆分为long集合
     * @param ids id集合
     * @param charStr 分隔符
     * @return
     */
    public static List<Long> splitLong(String ids, String charStr) {
        List<Long> list = new ArrayList<>();
        String[] strList = ids.split(charStr);
        for(String tmp : strList){
            if("".equals(tmp)){
                continue;
            }
            list.add(Long.parseLong(tmp));
        }
        return list;
    }

    public static boolean isBlank(String ip) {
        return StringUtils.isBlank(ip);
    }
}
