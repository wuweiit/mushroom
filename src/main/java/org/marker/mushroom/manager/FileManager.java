package org.marker.mushroom.manager;/**
 * Created by marker on 2018/9/25.
 */


import org.marker.mushroom.beans.ResultMessage;

import java.io.File;

/**
 * @author marker
 * @create 2018-09-25 13:09
 **/
public interface FileManager {


    /**
     * 删除文件
     * @param file
     * @return
     */
    ResultMessage delete(File file);


    /**
     * 检查提交路径
     * @param path
     * @return
     */
    boolean checkPath(String path);
}
