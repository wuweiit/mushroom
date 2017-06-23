/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
 

/**
 * @author marker
 * @date 2013-12-3 下午4:10:51
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class LogInterceptor  implements MethodInterceptor{

 
	@Override
	public Object intercept(Object arg0, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		return proxy.invoke(arg0, args);
	}

}
