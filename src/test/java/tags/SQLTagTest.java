package tags;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.ext.tag.impl.SqlExecuteTagImpl;
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
public class SQLTagTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void test(){


        SqlExecuteTagImpl sql = new SqlExecuteTagImpl();
        sql.iniContent("<!--{a:list sql=(select * from channel join t_asdasd on dsad.dasdsa=dsads)}--> \n" +
                " ");

        try {
            sql.doReplace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
        System.out.println(sql.getContent());





	} 	
}
 