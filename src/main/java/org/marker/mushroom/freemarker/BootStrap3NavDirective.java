package org.marker.mushroom.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.marker.develop.freemarker.MessageWrapperModel;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.SendDataToView;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * bootstrap3导航功能实现支持二级导航
 * 
 * 调用代码： <@Boostrap3Nav></@Boostrap3Nav>
 * 
 * @author marker
 * @version 1.0
 */
public class BootStrap3NavDirective implements TemplateDirectiveModel{



	private IChannelDao channelDao;
	
	
	public BootStrap3NavDirective() {
		channelDao = SpringContextHolder.getBean(DAO.CHANNEL); 
	}
	
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map  params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		MessageWrapperModel languageModel = (MessageWrapperModel) ActionContext.getReq().getAttribute(SendDataToView.KEY_MESSAGE_CONTEXT);
		
		List<Channel> list = channelDao.findValid();

		// 树形结构转换
		ChannelItem root = TreeUtils.foreach(new Channel(), list);


		// 获取URL重写实例
		URLRewriteEngine urlrewrite = SingletonProxyFrontURLRewrite.getInstance();



		StringBuilder str = new StringBuilder();

		String web = (String) ActionContext.getReq().getAttribute(AppStatic.WEB_APP_URL);

		str.append("<ul class=\"nav navbar-nav\">\n");
		
		
		List<ChannelItem> items = root.getChild();
		Iterator<ChannelItem> it = items.iterator();
		while(it.hasNext()){
			ChannelItem item = it.next();
			Channel c = item.getChannel();
			
			str.append("  <li>\n")
				.append("    <a ");
				boolean hasChild = item.hasChild();
				if(hasChild){
					str.append("data-toggle=\"dropdown\" class=\"dropdown-toggle\" ");
				}

				String url = urlrewrite.encoder("/cms?p=" + c.getUrl()+"&lang=" + WebParam.get().language);
				
				str.append("href=\"").append(url).append("\">");
				String name = c.getName();
                if(!StringUtils.isEmpty(c.getLangkey())){
					TemplateModel templateModel = languageModel.get(c.getLangkey()) ;
					if (Objects.nonNull(templateModel) ) {
						name = templateModel.toString();
					}
                }
				str.append(name);
				if(hasChild){
					str.append(" <span class=\"caret\"></span>\n"); 
					str.append("    <ul class=\"dropdown-menu\" role=\"menu\">\n"); 
					List<ChannelItem> citems = item.getChild();
					Iterator<ChannelItem> cit = citems.iterator();
					while(cit.hasNext()){
						ChannelItem citem = cit.next();
						Channel cc = citem.getChannel();
						String url2 = urlrewrite.encoder("/cms?p=" + cc.getUrl()+"&lang=" + WebParam.get().language);

						str.append("      <li><a href=\"").append(url2).append("\">");

						String name2 = cc.getName();
						if(!StringUtils.isEmpty(cc.getLangkey())){
							TemplateModel templateModel = languageModel.get(cc.getLangkey()) ;
							if (Objects.nonNull(templateModel) ) {
								name2 = templateModel.toString();
							}
						}
						str.append(name2);
						str.append("</a></li>\n");
					}
					str.append("    </ul>\n");
				}
				str.append("    </a>\n")
			.append("  </li>\n");
		}
		str.append("</ul>\n");
		 
		env.getOut().write(str.toString());
	}
	
}
