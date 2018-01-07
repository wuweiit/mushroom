package org.marker.mushroom.freemarker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import freemarker.core.Environment;
import freemarker.template.*;
import org.marker.mushroom.core.config.impl.SystemConfig;

import java.io.IOException;
import java.util.Map;


/**
 *  党建配置
 * @author marker
 * @version 1.0
 */
public class DangjianDirective implements TemplateDirectiveModel {

	public DangjianDirective() {
	}
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String index = params.get("index").toString();//
		int indexInt = Integer.parseInt(index);


		SystemConfig syscfg = SystemConfig.getInstance();

		String json = syscfg.getDangjian();



		JSONArray array = JSON.parseArray(json);


		env.setVariable("item", ObjectWrapper.DEFAULT_WRAPPER.wrap(array.get(indexInt)));
		body.render(env.getOut());
	}

}
