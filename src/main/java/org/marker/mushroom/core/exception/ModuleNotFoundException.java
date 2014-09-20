/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.core.exception;

/**
 * @author marker
 * @date 2013-9-21 下午9:41:06
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class ModuleNotFoundException extends SystemException {
	private static final long serialVersionUID = 3146568280686011780L;

	/**
	 * @param message
	 */
	public ModuleNotFoundException(String moduleType) {
		super("未找到内容模型 ："+moduleType);
	}

}
