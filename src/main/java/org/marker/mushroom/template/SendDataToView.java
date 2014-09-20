package org.marker.mushroom.template;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.ext.message.MessageContext;
import org.marker.mushroom.ext.plugin.freemarker.EmbedDirectiveInvokeTag;
import org.marker.mushroom.freemarker.LoadDirective;
import org.marker.mushroom.freemarker.UpperDirective;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.mushroom.utils.WebUtils;
import org.marker.urlrewrite.freemarker.FrontURLRewriteMethodModel;

import freemarker.template.Template;
import freemarker.template.TemplateModelException;

public class SendDataToView {
	/** 系统配置信息 */
	private static final SystemConfig syscfg = SystemConfig.getInstance();

	/** 国际化 */
	private static final MessageContext mc = MessageContext.getInstance();
	HttpServletRequest request;
	
	HttpServletResponse response;
	ServletContext application;
	
	String tpl;
	
	public SendDataToView(String tpl) { 
		this.tpl = tpl;
		request      = ActionContext.getReq();
		response     = ActionContext.getResp();
		application  = ActionContext.getApplication();
	}

	public void process() {

		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
}
