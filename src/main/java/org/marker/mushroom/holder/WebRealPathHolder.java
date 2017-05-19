package org.marker.mushroom.holder;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

/**
 * 用静态变量保存web真实路径，方便其他类调用
 * 
 * @author marker
 * @since 1.0
 */
public class WebRealPathHolder implements ServletContextAware {

	/** Web应用真实路径 */
	public static String REAL_PATH;

	@Override
	public void setServletContext(ServletContext sc) {
		REAL_PATH = sc.getRealPath(File.separator);
	}

}
