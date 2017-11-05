package tags;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestParret {

	public static void main(String[] args) {
		
		
		Pattern p = Pattern.compile("(jpg|png|htm)");
		Matcher me = p.matcher("htm");
		System.out.println(me.matches());
	}

	@Test
	public void test(){
		String text2 ="limit=(1,1) ";

		Pattern p_a = Pattern.compile("\\w+\\=\\('?\\w*[\\x20,]?\\d*\\w*'?"); // 将给定的正则表达式编译到模式中
		Matcher m_a = p_a.matcher(text2);
		m_a.find();
		System.out.printf(m_a.group());
	}

	@Test
	public void test1(){
//		String str = "D:\\servers\\tomcat\\webapps\ROOT\\themes\\mrcms\\";
//
//
//		System.out.println(str);
//		"".replaceAll(str,"");
	}
}
