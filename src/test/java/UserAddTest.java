import org.marker.mushroom.utils.GeneratePass;
import org.marker.security.Base64;
import org.marker.security.DES;
import org.marker.security.MD5;


public class UserAddTest {

	
	public static void main(String[] args) throws Exception {
		String key = "5uDpKkZnemE\\=";
		 
		 String a = MD5.getMD5Code(Base64.encode(DES.encrypt(
					"714624741".getBytes(), key))); 
		 System.out.println(a);
	}
}
