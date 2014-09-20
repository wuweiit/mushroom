import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ZZTest {
	public class data{
		public int start;
		public int end;
	}

	public static void main(String[] args) {
		
		Pattern p = Pattern.compile("(\\d+)");
		String content = "131513158---151566---132113123";
		
		
		StringBuffer sb = new StringBuffer(content);
		Matcher m = p.matcher(sb);
		
		
		List<data> list = new ArrayList<data>();
		while(m.find()){
			System.out.println(m.group());
			int start = m.start();
			int end   = m.end(); 
			sb.replace(start, end, UUID.randomUUID().toString());
	 
		}
		
	 
		
		
		System.out.println(sb);
		 
		
		
	}
}
