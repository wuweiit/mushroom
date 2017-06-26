/**
 * 
 */
package tags;

import org.junit.Test;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.ext.tag.impl.ChildChannelTagImpl;
import org.marker.mushroom.ext.tag.impl.LoopTagImpl;
import org.marker.mushroom.utils.FileTools;

import java.io.File;
import java.io.IOException;


/**
 * 
 * 单元测试模块
 * 
 * @author marker 
 */ 
public class ChildTagImplTest {

	
	@Test
	public void testMain() throws IOException { 
		Taglib tag = new ChildChannelTagImpl();
		String c = FileTools.getFileContet(new File("/WORK/git/mushroom/src/test/java/tags/res/child.txt"), FileTools.FILE_CHARACTER_UTF8);

		tag.iniContent(c);
		try {
			tag.doReplace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		System.out.println("========== ");
		System.out.println(tag.getContent());

	}
	
 
}
