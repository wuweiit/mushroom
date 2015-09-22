package unit.tags;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.ext.tag.impl.ListTagImpl;
import org.marker.mushroom.utils.FileTools;
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
public class ListTest  {
	
	
	@Test
	public void test() throws IOException{
		Taglib tag = new ListTagImpl();
		String c = FileTools.getFileContet(new File(getFile("./res/list.txt")), FileTools.FILE_CHARACTER_UTF8);
		
		tag.iniContent(c);
		try {
			tag.doReplace(); 
		} catch (SystemException e) {
			e.printStackTrace();
		}
		System.out.println(tag.getContent()); 
	}
	 
	public String getFile(String fileName){
		return ListTest.class.getResource(fileName).getFile();
	}
}
 