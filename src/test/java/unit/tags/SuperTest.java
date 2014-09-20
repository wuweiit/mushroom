package unit.tags;

import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

public class SuperTest extends AbstractJUnit4SpringContextTests{

	
	
	
	
	public String getFile(String fileName){
		return SuperTest.class.getResource(fileName).getFile();
	}
}
