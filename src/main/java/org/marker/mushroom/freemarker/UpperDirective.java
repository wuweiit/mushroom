/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.freemarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.marker.mushroom.beans.Channel;

import freemarker.core.Environment;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author marker
 * @date 2013-11-5 下午2:43:28
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 * sql指令来获取数据
 */
public class UpperDirective implements TemplateDirectiveModel {

	/*
	 * 
	 */
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		 
		
		
		 
		String sql = params.get("sql").toString(); 
		System.out.println(loopVars.length); 
		Channel r = new Channel() ;
		 r.setName("dsadsadasd");
		List a =  new ArrayList();
		a.add(r);a.add(r);a.add(r);a.add(r);a.add(r);
		loopVars[0] = new SimpleCollection(a); 
		body.render(env.getOut());
		 
	}
}
