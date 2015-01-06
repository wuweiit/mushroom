import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marker.mushroom.utils.SQLUtil;



public class ZZTest {
	public class data{
		public int start;
		public int end;
	}

	public static void main(String[] args) {
		
		String a = new SQLUtil().format("select * from user a join left group b on a=a");
		System.out.println(a);
	}
}
