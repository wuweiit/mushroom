/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.core.exception;

/**
 * @author marker
 * @date 2013-9-21 下午9:48:55
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class ModuleNotNewInstanceException extends SystemException {

	private static final long serialVersionUID = -2006627632505472444L;

	/**
	 * @param message
	 */
	public ModuleNotNewInstanceException(String message) {
		super(message);
	}

}
