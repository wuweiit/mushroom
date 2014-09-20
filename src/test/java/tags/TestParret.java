package tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestParret {

	public static void main(String[] args) {
		
		
		Pattern p = Pattern.compile("(jpg|png|htm)");
		Matcher me = p.matcher("htm");
		System.out.println(me.matches());
	}
}
