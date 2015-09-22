package unit.services;
/**
 *  
 *  吴伟 版权所有
 */


import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.service.impl.CategoryService;
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
@ContextConfiguration(locations = "classpath:/config/spring/*.xml")
public class CategoryServiceTest extends AbstractJUnit4SpringContextTests {
 
	
	@Autowired CategoryService categoryService;
	
	@Test
	public void test(){
//		
//		String model = categoryService.findModelById(1);
//		Assert.assertEquals("test category 1 model!=article","article", model); 
//
//		String model2 = categoryService.findModelById(2);
//		Assert.assertEquals("test category 2 model!=article","article", model2); 
//	
//		
//		List<Map<String, Object>> list = categoryService.list();
//	
//		Assert.assertNotNull("category is null!", list);
//		
//		List<Map<String, Object>> list2 = categoryService.list("article");
//		
//		Assert.assertNotNull("category is null!", list2);
//		
//		System.out.println(""+list2);
//		
//		
//		
//		boolean a = categoryService.hasChild(19);
//		Assert.assertTrue("19 not has child", a);
//		boolean b = categoryService.hasChild(256156);
//		Assert.assertFalse("256156 not has child", b);
		
	}
	 
 
}
 