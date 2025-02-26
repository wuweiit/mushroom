
package org.marker.mushroom.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 *
 * @author marker
 * @date 2013-8-24 下午3:52:13
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	@Override
	@Resource
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBean(clazz);
	}



	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}


    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }


    /**
     * 获取配置
     * @param key key
     * @return
     */
    public static String getProperty(String key){
        Properties pro = getBean("configProperties");
        return pro.getProperty(key);
    }


    /**
     * 获取配置int
     *
     * @param key key
     */
    public static int getPropInt(String key) {
        return Integer.valueOf(getProperty(key));
    }



	/**
	 * 是否为生产环境
	 * @return
	 */
	public static boolean isProduction(){
		Environment env = getBean(Environment.class);
		String active = env.getProperty("spring.profiles.active");
		if("prod".equals(active)){
			return true;
		}
		return false;
	}

	/**
	 * 是否为生产环境
	 * @return
	 */
	public static String getProfilesActive(){
		Environment env = getBean(Environment.class);
		String active = env.getProperty("spring.profiles.active");
		return active;
	}

	/**
	 * 是否演示模式
	 * @return
	 */
	public static boolean isDemo(){
		Environment env = getBean(Environment.class);
		String isDemo = env.getProperty("mrcms.demo");
		if("true".equals(isDemo)){
			return true;
		}
		return false;
	}
}
