package unit.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.beans.Menu;
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
@ContextConfiguration(locations = "classpath:/config/spring/*.xml")
public class MenuDaoTest extends AbstractJUnit4SpringContextTests {
 
	// 自动注入Dao对象
	@Autowired IMenuDao menuDao;
	
	@Test
	public void test(){
		Menu m = menuDao.findByName("文章管理");
		Assert.assertNotNull("is null ", m); 
	} 	
}
 