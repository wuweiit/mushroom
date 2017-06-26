package unit.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.dao.IMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试
 * @author marker 
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:/config/spring/application.xml")
public class ConfigDBEngineTest extends AbstractJUnit4SpringContextTests {

	// 自动注入Dao对象
	@Autowired
    SystemConfig systemConfig;

    // 自动注入Dao对象
    @Autowired
    URLRewriteConfig urlRewriteConfig;
	
	@Test
	public void test(){
		String file = "/WORK/git/cms/src/main/resources/config/site/system.config";
        systemConfig.loadFile(file);

        systemConfig.store();

	}


    @Test
    public void test2(){
        String file = "/WORK/git/cms/src/main/resources/config/urlrewrite/urlrewrite.config";
        urlRewriteConfig.loadFile(file);
        urlRewriteConfig.store();
    }
}
 