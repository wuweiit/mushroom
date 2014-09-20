
package org.marker.mushroom.core.exception;

/**
 * 模板文件未找到异常
 * @author marker
 * @date 2013-8-24 下午12:42:17
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class TemplateFileNotFoundException extends SystemException {

	private static final long serialVersionUID = -8469032613028009433L;

	/**
	 * @param message
	 */
	public TemplateFileNotFoundException() {
		super("模板文件未找到");
	}

	/**
	 * @param tplFileName
	 * @param childTemplateFileName
	 */
	public TemplateFileNotFoundException(String tplFileName,
			String childTemplateFileName) {
		super(new StringBuilder("模板文件 \"").append(tplFileName).append("\" 中的引用\"").append(childTemplateFileName).append("\"文件未找到!").toString());
	}

	
	
	/**
	 * @param tplFileName
	 */
	public TemplateFileNotFoundException(String tplFileName) {
		super(new StringBuilder("模板文件 \"").append(tplFileName).append("\" 未找到!").toString());
	}

}
