package utils;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.marker.mushroom.utils.FileUtils;

import java.util.List;

/**
 *
 * Ip转换物理地址工具测试
 *
 * @author marker
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 * Created by marker on 16/10/22.
 */
public class FileToolTest {


    /**
     * 路径获取测试
     */
    @Test
    public void test(){


        List<String> list = FileUtils.getPathList("/WORK/git/mushroom/src/main/webapp/themes/flatweb/", "html");
        System.out.println(JSON.toJSONString(list, true));

    }

}
