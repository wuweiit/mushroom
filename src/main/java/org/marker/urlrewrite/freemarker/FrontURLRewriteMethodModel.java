package org.marker.urlrewrite.freemarker;

import java.util.List;

import freemarker.ext.beans.StringModel;
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
    // URL编码前的前缀
	public static final String CMS_TAG = SingletonProxyFrontURLRewrite.URL_PATTERN + "p=";
	/**
	 * URL重写处理方法
	 */
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String fakeUrl = "";
		if (args != null && args.size() > 0) {
			Object obj = args.get(0);

			if(obj instanceof SimpleScalar){
				SimpleScalar realUrl = (SimpleScalar) obj;
				fakeUrl = urlrewrite.encoder(urlrewrite.getUrlPattern()+realUrl);
			}else if(obj instanceof StringModel){
				StringModel realUrl = (StringModel) obj;
				String baseUrl = urlrewrite.getUrlPattern()  + realUrl;
				fakeUrl = urlrewrite.encoder(baseUrl);
			}

		}
		return fakeUrl;
	}

}
