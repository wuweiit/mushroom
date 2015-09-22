package test.spring;
/**
 *  
 *  吴伟 版权所有
 */


import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用户数据库操作对象测试
 * @author marker
 * @date 2013-11-29 上午9:47:26
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = "classpath:/config/spring/*.xml")
public class ThemesTest extends AbstractJUnit4SpringContextTests {
 
	
 
	
	@Test
	public void sendMail(){ 
//		System.out.println("test....");
//		String themesPath = WebRealPathHolder.REAL_PATH + File.separator+"themes/";
//		
//		File file = new File(themesPath);
//		
//		System.out.println(themesPath);
//		String[] filelist = file.list();
//		for(String f : filelist ){
//			
//			System.out.println(f);
//		}
//		
	}
	 
 
}
 