package template;

import java.io.File;
import java.io.IOException;

import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.template.TemplateFileLoad;

public class Test {

	public static void main(String[] args) { 
		long a = System.currentTimeMillis();
		WebRealPathHolder.REAL_PATH = "D:\\Servers\\tomcat6\\webapps\\ROOT\\";
		boolean aa = true;
		while(aa){
			try {
				new TemplateFileLoad(new File("D:\\Servers\\tomcat6\\webapps\\ROOT\\themes\\flatweb\\index.html"));
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		
		System.out.println("耗时："+(System.currentTimeMillis() - a));
		
		
		
	}
}
