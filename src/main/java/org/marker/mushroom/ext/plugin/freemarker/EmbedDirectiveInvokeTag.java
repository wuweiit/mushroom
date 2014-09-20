package org.marker.mushroom.ext.plugin.freemarker;

import java.util.List;

import org.marker.mushroom.ext.plugin.PluginContext;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;


/**
 * 嵌入式指令调用标签(for freemarker)
 * 
 * @author marker
 * */
public class EmbedDirectiveInvokeTag implements TemplateMethodModel {
 
 
	@Override
	public Object exec(@SuppressWarnings("rawtypes") List args) throws TemplateModelException {
		String errorStr = "<!-- 插件调用错误！ -->"; 
		if(args != null && args.size() > 1){
			String pluginName = (String) args.get(0);
			String directive  = (String) args.get(1);
			
			// 获取插件作用域
			PluginContext pluginContext = PluginContext.getInstance();
			try {
				return pluginContext.invoke(pluginName, directive);
			} catch (Exception e) { 
				return errorStr + " Because Of Exception";
			}
		}
		return errorStr;
	} 

}
