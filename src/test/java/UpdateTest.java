import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.marker.mushroom.dao.annotation.Entity;

/**
 *  
 *  吴伟 版权所有
 */

/**
 * @author marker
 * @date 2013-12-3 下午12:57:18
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class UpdateTest {

	@org.junit.Test
	public void testUpdate(){
		User user = new User();
		user.setName("wuwei");
		user.setId(1);
		user.setHide(1);
		user.setPass("123456");
		
		Class<?> clzz = user.getClass();
		Entity tableInfo = clzz.getAnnotation(Entity.class);
		String tableName = tableInfo.value();
		String primaryKey = tableInfo.key();

		StringBuffer sql = new StringBuffer();
		sql.append("update `").append(tableName)
				.append("` set ");
		List<Object> list = null;
		long id = 0;
		try {
			id = (Long) clzz.getMethod("getId").invoke(user);
			Field[] fields = clzz.getDeclaredFields();
			int length = fields.length; 
			list = new ArrayList<Object>(length);
			for (int i = 0; i < length; i++) {
				System.out.println(fields[i].getName());
				Field field = fields[i];
				String fieldName = field.getName();
				if (fieldName.equals(primaryKey)) {// 如果是主键
					continue;
				}
				String methodName = "get" + fieldName.replaceFirst(fieldName.charAt(0) + "",
								(char) (fieldName.charAt(0) - 32) + "");
				Method me = clzz.getMethod(methodName);
				Object returnObject = me.invoke(user);
				if (returnObject != null) {// 如果返回值为null
					sql.append("`" + fieldName + "`").append("=?");
					list.add(returnObject); 
					sql.append(","); 
				}
			}

			sql = new StringBuffer(sql.substring(0, sql.length()-1));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.add(id);
		sql.append(" where `").append(primaryKey).append("`=?"); 
		System.out.println(sql.toString());
		
		
	}
}
