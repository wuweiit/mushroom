package test.spring;
/**
 *  
 *  吴伟 版权所有
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用户数据库操作对象测试
 * @author marker
 * @date 2013-11-29 上午9:47:26
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:/config/spring/application.xml")
public class ConfigServiceTest extends AbstractJUnit4SpringContextTests {
 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void test(){

//        URLRewriteConfig systemConfig = new URLRewriteConfig(jdbcTemplate);


//		systemConfig.storeFile(new File("./b.txt"));
	}



    @Test
    public void testLoad() throws FileNotFoundException {
        String yamlStr = "key: hello yaml";

        String aa = "/WORK/git/mushroom/src/main/webapp/WEB-INF/conf/site.yml";
        InputStream is = new FileInputStream(aa);
        Yaml yaml = new Yaml();
        Properties ret = yaml.loadAs(is, Properties.class );
        String a = yaml.dumpAsMap(  ret );
        System.out.println(a);
        System.out.println(ret);

        System.out.println(ret.get("mrcms.site.title"));
    }

 
}
 