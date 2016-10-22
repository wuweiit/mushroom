import org.junit.Test;
import org.marker.qqwryip.IPLocation;
import org.marker.qqwryip.IPTool;

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
public class IPToolTest {



    /**
     * 测试本地IP地址转换
     *
     */
    @Test
    public void test(){
        IPTool tool = IPTool.getInstance();
        IPLocation location = tool.getLocation("192.168.1.1");
        System.out.println(location);
    }
}
