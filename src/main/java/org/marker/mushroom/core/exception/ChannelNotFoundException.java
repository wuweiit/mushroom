/**
 * 
 */
package org.marker.mushroom.core.exception;

/**
 * @author marker
 * @date 2013-8-24 下午2:37:50
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class ChannelNotFoundException extends SystemException {

	
	private static final long serialVersionUID = -5424721060607537480L;

	/**
	 * @param message
	 */
	public ChannelNotFoundException(String param) {
		super("数据库中未找到栏目：".concat(param));
	}

}
