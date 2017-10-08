package org.marker.mushroom.freemarker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import freemarker.core.Environment;
import freemarker.template.*;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.impl.CategoryService;

import java.io.IOException;
import java.util.Map;


/**
 * JSON解析指令
 * @author marker
 * @version 1.0
 */
public class JSONPaserDirective implements TemplateDirectiveModel {

	public JSONPaserDirective() {
	}
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String jsonStr = params.get("json").toString();//
		JSONArray array = JSON.parseArray(jsonStr);
		env.setVariable("list", ObjectWrapper.DEFAULT_WRAPPER.wrap(array.toArray()));
		body.render(env.getOut());
	}

}
