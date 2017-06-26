package unit.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.ext.message.MessageDBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * 单元测试
 * @author marker 
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:/config/spring/application.xml")
public class ContextDBMessageTest extends AbstractJUnit4SpringContextTests {

	// 自动注入Dao对象
	@Autowired
    MessageDBContext messageDBContext;

	
	@Test
	public void test() throws IOException {
        messageDBContext.loadFile("default", "/WORK/git/cms/src/main/webapp/data/international/message.properties");
        messageDBContext.loadFile("en", "/WORK/git/cms/src/main/webapp/data/international/message_en.properties");
        messageDBContext.loadFile("ko", "/WORK/git/cms/src/main/webapp/data/international/message_ko.properties");
        messageDBContext.loadFile("zh-CN", "/WORK/git/cms/src/main/webapp/data/international/message_zh-CN.properties");
        messageDBContext.loadFile("zh-HK", "/WORK/git/cms/src/main/webapp/data/international/message_zh-HK.properties");

        messageDBContext.storeProperty();

	}


}
 