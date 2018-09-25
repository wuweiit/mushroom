package org.marker.mushroom.manager;/**
 * Created by marker on 2018/9/25.
 */

import org.marker.mushroom.beans.ResultMessage;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 文件管理器实现
 *
 * @author marker
 * @create 2018-09-25 13:09
 **/
@Service
public class FileManagerImpl implements FileManager {




    @Override
    public ResultMessage delete(File file) {

        if(!file.exists()){
            return new ResultMessage(true, "文件不存在!");
        }

        if(file.delete()){
            return new ResultMessage(true, "删除成功!");
        }else{
            return new ResultMessage(false, "删除失败!");
        }
    }

    @Override
    public boolean checkPath(String path) {
        if(path.indexOf("..") != -1){
            return false;
        }
        return true;
    }
}
