import org.junit.Test;
import org.marker.mushroom.utils.GeneratePass;

/**
 * Created by marker on 16/11/25.
 */
public class PasswordTest {

    @Test
    public void test() throws Exception {
        String pass2 = GeneratePass.encode("1");
        System.out.printf(pass2);
    }
}
