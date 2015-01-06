package unit.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
public class UserGroupDaoTest {
 
	// 自动注入Dao对象
	@Autowired IUserDao userDao;
	
	
	
	@Test
	public void test(){
		System.out.println(userDao.countUserByGroupId(1));
	} 	
}
 