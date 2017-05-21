package org.marker.urlrewrite.freemarker;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;

import freemarker.template.TemplateModelException;

/**
 * URL重写标签(for freemarker) ${encoder("/cms?p=index")} 前端URL重写方法模型
 * 
 * @author marker
 * */
public final class FrontURLRewriteMethodModel implements TemplateMethodModelEx {

	// 获取URL重写实例
	private final URLRewriteEngine urlrewrite = SingletonProxyFrontURLRewrite.getInstance();

	/**
	 * URL重写处理方法
	 */
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String fakeUrl = "";
		if (args != null && args.size() > 0) {
			SimpleScalar realUrl = (SimpleScalar) args.get(0);

			fakeUrl = urlrewrite.encoder(urlrewrite.getUrlPattern()+realUrl); 
		}
		return fakeUrl;
	}

}
