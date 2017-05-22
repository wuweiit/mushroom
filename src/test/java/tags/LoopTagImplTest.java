/**
 * 
 */
package tags;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.ext.tag.impl.LoopTagImpl;
import org.marker.mushroom.utils.FileTools;


/**
 * 
 * 单元测试模块
 * 
 * @author marker 
 */ 
public class LoopTagImplTest {

	
	@Test
	public void testMain() throws IOException { 
		Taglib tag = new LoopTagImpl();
		String c = FileTools.getFileContet(new File("/Users/marker/git/mushroom/src/test/java/tags/res/looptag.txt"), FileTools.FILE_CHARACTER_UTF8);

		tag.iniContent(c);
		try {
			tag.doReplace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		System.out.println(tag.getContent());

	}
	
 
}
