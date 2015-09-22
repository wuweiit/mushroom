package test.uuid;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 标签单元测试
 * @author marker 
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:/config/spring/*.xml")
public class UUIDTest {
	
	
	@Test
	public void test() throws IOException{

		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}
	 
 
}
 