/**
 *  
 *  吴伟 版权所有
 */

/**
 * @author marker
 * @date 2013-11-15 下午9:03:43
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class FinalTest {

	final 
	
	public static void main(String[] args) {
		new Usaaa();new Usaaa();
		System.out.println("aa");
		
	}
}

class Usera{
	public Usera(){
		System.out.println("ddd");
	}
}

class Usaaa{
	
	static final Usera a = new Usera();
	
	
}
