package org.marker.develop.freemarker;

import java.util.Properties;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;



/**
 * 消息包装模型
 * @author marker
 * @version 1.0
 */
public class MessageWrapperModel implements TemplateHashModel {

	 private final Properties pro;
	 private final ObjectWrapper wrapper; 
	 
	 
	 
	 
	
	public MessageWrapperModel(Properties pro, ObjectWrapper wrapper) { 
		this.pro = pro;
		this.wrapper = wrapper; 
	}

	
	
	
	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if(key == null){
			return wrapper.wrap(null);
		} 
		if(pro != null){
			String val = pro.getProperty(key);
			return wrapper.wrap(val);
		}
		return wrapper.wrap(null);
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return pro == null || pro.isEmpty();
	}
	
	
	
	
}
