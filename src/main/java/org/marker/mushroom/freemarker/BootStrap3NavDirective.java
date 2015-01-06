package org.marker.mushroom.freemarker;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.marker.develop.freemarker.MessageWrapperModel;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.ChannelTree;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.SendDataToView;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;






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
		
		MessageWrapperModel model = (MessageWrapperModel) ActionContext.getReq().getAttribute(SendDataToView.KEY_MESSAGE_CONTEXT);
		
		List<Channel> list = channelDao.findValid();
		
		ChannelItem root = ChannelTree.foreach(new Channel(), list);
		

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
				
				
				str.append("href=\"").append(web+"/"+c.getUrl()+".html").append("\">").append(model.get(c.getLangkey()));
				if(hasChild){
					str.append(" <span class=\"caret\"></span>\n"); 
					str.append("    <ul class=\"dropdown-menu\" role=\"menu\">\n"); 
					List<ChannelItem> citems = item.getChild();
					Iterator<ChannelItem> cit = citems.iterator();
					while(cit.hasNext()){
						ChannelItem citem = cit.next();
						Channel cc = citem.getChannel(); 
						str.append("      <li><a href=\"").append(web+"/"+cc.getUrl()+".html").append("\">").append(model.get(cc.getLangkey())).append("</a></li>\n");
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
