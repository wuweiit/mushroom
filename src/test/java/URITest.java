import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marker on 2017/7/1.
 */
public class URITest {



    @Test
    public void test(){
        String uri = "/admin/logiIinfo.do";
        Pattern pattern = Pattern.compile("/admin/(\\w+)");
        Matcher matcher = pattern.matcher(uri);

        String loginPath = matcher.find()?matcher.group(1):"";
        System.out.println(loginPath);
    }
}
