package org.marker.mushroom.holder;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * 用静态变量保存web真实路径，方便其他类调用
 * 
 * @author marker
 * @since 1.0
 */
public class WebRealPathHolder implements ServletContextAware {

	/** Web应用真实路径 */
	public static String REAL_PATH;
	public static String CONTEXT_PATH;

	@Override
	public void setServletContext(ServletContext sc) {
		String webRootPath = sc.getRealPath("");
		String xgStr = webRootPath.substring(webRootPath.length() -1);
        if(!File.separator.equals(xgStr)){
			webRootPath += File.separator;
		}
        REAL_PATH = webRootPath;
        CONTEXT_PATH = sc.getContextPath();

	}

}
