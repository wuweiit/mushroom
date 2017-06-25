package org.marker.mushroom.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.marker.develop.freemarker.MessageWrapperModel;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.channel.CategoryItem;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.template.SendDataToView;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 華西四院導航 支持二級導航
 *
 * （手机导航显示输出）
 *
 *
 *
 * 
 * 调用代码： <@HuaXiSiYuanNav></@Boostrap3Nav>
 * 
 * @author marker
 * @version 1.0
 */
public class HuaxiSiYuanNavDirective implements TemplateDirectiveModel{



	private IChannelDao channelDao;


	public HuaxiSiYuanNavDirective() {
		channelDao = SpringContextHolder.getBean(DAO.CHANNEL); 
	}
	
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map  params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		MessageWrapperModel languageModel = (MessageWrapperModel) ActionContext.getReq().getAttribute(SendDataToView.KEY_MESSAGE_CONTEXT);
		
		List<Channel> list = channelDao.findValid();
		
		ChannelItem root = TreeUtils.foreach(new Channel(), list);
		

		StringBuilder str = new StringBuilder();

		String web = (String) ActionContext.getReq().getAttribute(AppStatic.WEB_APP_URL);



		URLRewriteConfig urlConfig =  URLRewriteConfig.getInstance();
		String pageSufffix = urlConfig.getPageSuffix();// 页面后缀



		str.append("<ul id=\"filters\" class=\"sort-ul\">\n");
		
		
		List<ChannelItem> items = root.getChild();
		Iterator<ChannelItem> it = items.iterator();
		while(it.hasNext()){
			ChannelItem item = it.next();
			Channel c = item.getChannel();
			
			str.append("   <li class=\"sort-li\">\n")
				.append("    <h3 class=\"sort-li-title\"><a ");

				str.append("href=\"").append(web+"/"+c.getUrl()).append(pageSufffix).append("\">");
                if(!StringUtils.isEmpty(c.getLangkey())){
                    str.append(languageModel.get(c.getLangkey()));
                }else{
                    str.append(c.getName());
                }
                str.append("    </a></h3>\n");
                str.append("    <div class=\"sort-li-detail\">\n");
                if("dept".equals(c.getUrl())){// 科室输出


                    CategoryService categoryService = SpringContextHolder.getBean(Services.CATEGORY);
                    List<CategoryItem> childList = new ArrayList<>();

                    CategoryItem deptRoot = categoryService.getAllTree();

                    List<CategoryItem> categorys = deptRoot.getChild();

                    Iterator<CategoryItem> it2 = categorys.iterator();
                    while(it2.hasNext()) {
                        CategoryItem categoryItem = it2.next();
                        childList.addAll(categoryItem.getChild());
                    }


                    Iterator<CategoryItem> it3 = childList.iterator();
                    while(it3.hasNext()) {
                        CategoryItem categoryItem = it3.next();
                        Category category = categoryItem.getNode();

                        str.append("     <a class=\"btn-tag\" href=\"").append(web+"/category/"+category.getId()).append(pageSufffix).append("\">");
                        str.append(category.getName());
                        str.append("</a>\n");
                    }
				}else{
					List<ChannelItem> citems = item.getChild();
					Iterator<ChannelItem> cit = citems.iterator();
					while(cit.hasNext()){
						ChannelItem citem = cit.next();
						Channel cc = citem.getChannel();
						str.append("     <a class=\"btn-tag\" href=\"").append(web+"/"+cc.getUrl()).append(pageSufffix).append("\">");
						if(!StringUtils.isEmpty(cc.getLangkey())){
							str.append(languageModel.get(cc.getLangkey()));
						}else{
							str.append(cc.getName());
						}
						str.append("</a>\n");
					}
				}
				str.append("    </div>\n")
			.append("  </li>\n");
		}
		str.append("</ul>\n");
		 
		env.getOut().write(str.toString());
	}
	
}
