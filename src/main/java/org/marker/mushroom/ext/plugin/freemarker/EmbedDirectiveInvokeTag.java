package org.marker.mushroom.ext.plugin.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.*;
import org.marker.mushroom.ext.plugin.PluginContext;


/**
 * 嵌入式指令调用标签(for freemarker)
 *
 *
 * 调用指定的方法
 * 案例：
 *  <@Plugin pluginName="$1" invoke="$2"></@Plugin>
 *
 * @author marker
 * */
public class EmbedDirectiveInvokeTag implements TemplateDirectiveModel {
 
 
	@Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map  params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
		String resultStr = "<!-- 插件调用错误！ -->";


        String pluginName = (String) params.get("pluginName").toString();
        String method = (String) params.get("invoke").toString();

        // 获取插件作用域
        PluginContext pluginContext = PluginContext.getInstance();
        try {
            resultStr = pluginContext.invokeMethod(pluginName, method);
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = resultStr + " Because Of Exception";
        }
        env.getOut().write(resultStr);

	} 

}
