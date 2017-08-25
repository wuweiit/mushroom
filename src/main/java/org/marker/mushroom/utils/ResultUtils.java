package org.marker.mushroom.utils;
/**
 * Created by marker on 2017/8/25.
 */

import org.marker.mushroom.beans.ResultMessage;

/**
 * 返回消息工具类
 *
 * @author marker
 * @create 2017-08-25 下午10:06
 **/
public final class ResultUtils {


    /**
     * 错误消息
     * @param msg 消息内容
     * @return ResultMessage
     */
    public static ResultMessage error(String msg) {
        return new ResultMessage(false, msg);
    }


    /**
     * 操作成功消息
     *
     * @param msg 消息内容
     * @return ResultMessage
     */
    public static ResultMessage success(String msg) {
        return new ResultMessage(true, msg);
    }
}
