package org.marker.mushroom.utils;/**
 * Created by marker on 2017/9/2.
 */

import org.marker.mushroom.core.config.impl.SystemConfig;

import java.io.File;
import java.util.List;

/**
 * @author marker
 * @create 2017-09-02 下午5:59
 **/
public final class TemplateUtils {


    /**
     * 获取模板文件列表
     * @return List<String>
     */
    public static List<String> getTempalteFiles() {
        SystemConfig syscfg = SystemConfig.getInstance();
        String path = syscfg.getThemesPath() + File.separator + syscfg.getThemeActive() + File.separator;
        return FileUtils.getPathList(path, "html");
    }
}
