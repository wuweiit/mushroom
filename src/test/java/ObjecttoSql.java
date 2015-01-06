import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.marker.mushroom.beans.User;
import org.marker.mushroom.dao.annotation.Entity;

public class ObjecttoSql {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		User u = new User();
		u.setLogintime(new Date());
		u.setName("admin");
		u.setPass("123456");
		long a = System.currentTimeMillis();
		Class<?> clzz = u.getClass();
		String tableName = u.getClass().getAnnotation(Entity.class).value();
		String primaryKey = clzz.getAnnotation(Entity.class).key();
 
		// 构造SQL字符串
		StringBuilder sql = new StringBuilder("insert into `")
				.append(tableName).append("`(");
		StringBuilder val = new StringBuilder(" values(");
 
		Field[] fields = u.getClass().getDeclaredFields(); 
		int length = fields.length;
		for (int i = 0; i < length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			if (fieldName.equals(primaryKey)) {// 如果是主键
				continue;
			}
			String methodName = "get"
					+ fieldName.replaceFirst(fieldName.charAt(0) + "",
							(char) (fieldName.charAt(0) - 32) + "");

			Method me = clzz.getMethod(methodName);
			Object returnObject = me.invoke(u);//
			if (returnObject != null) {// 如果返回值为null
				sql.append("`" + fieldName + "`");
				val.append("?");
				if (i < (length - 1)) {
					sql.append(", ");
					val.append(", ");
				}
			}
		}
		sql.append(")").append(val).append(")");

		System.out.println(sql);
		System.out.println( System.currentTimeMillis()-a);
	}
}
