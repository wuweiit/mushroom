package com.wuweibi.module4j.config;
/**
 * Created by marker on 2019/4/9.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.module4j.exception.PackageJsonErrorException;
import com.wuweibi.module4j.exception.PackageJsonNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 *
 * 模块包信息
 *
 *
 * @author marker
 *  2019-04-09 10:07
 **/
public class PackageInfo extends JSONObject {

    /** 文本文件UTF-8编码 */
    public static final String CHARACTER = "UTF-8";



    public PackageInfo(){ }


    /**
     *
     * 解析配置文件
     * @param jsonConfigFilePath json配置文件路径
     * @return
     * @throws PackageJsonNotFoundException json 配置文件没找到异常
     * @throws PackageJsonErrorException json 配置文件格式错误
     */
    public static PackageInfo parseString(String jsonConfigFilePath) throws PackageJsonNotFoundException, PackageJsonErrorException {

        String json = "{\"error\":\"invalid config info\"}";
        try {
            File filePackageJson = new File(jsonConfigFilePath);
            json = FileUtils.readFileToString(filePackageJson, CHARACTER);
        } catch (IOException e) {
            throw new PackageJsonNotFoundException();
        }

        JSONObject config = JSON.parseObject(json);
        if (config.containsKey("error")){
            throw new PackageJsonErrorException();
        }

        PackageInfo info = new PackageInfo();
        info.putAll(config);
        return info;
    }


    /**
     * 获取ID
     * @return String
     */
    public String getId() {
        return (String) this.get("id");
    }


    /**
     * 获取启动入口
     * @return  String
     */
    public String getActivatorFile() {
        String activator = this.getString("activator");
        if (StringUtils.isBlank(activator)){
            return "/src/activator";
        }
        return activator.replaceFirst(".groovy", "");
    }
}
