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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 華西四院導航 支持二級導航
 *
 * （PC导航显示输出）
 *
 *
 *
 * 
 * 调用代码： <@HuaXiSiYuanPCNav></@HuaXiSiYuanPCNav>
 * 
 * @author marker
 * @version 1.0
 */
public class HuaxiSiYuanPCNavDirective implements TemplateDirectiveModel{



	private IChannelDao channelDao;


	public HuaxiSiYuanPCNavDirective() {
		channelDao = SpringContextHolder.getBean(DAO.CHANNEL); 
	}
	
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map  params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		MessageWrapperModel languageModel = (MessageWrapperModel) ActionContext.getReq().getAttribute(SendDataToView.KEY_MESSAGE_CONTEXT);
		
		List<Channel> list = channelDao.findValid();
		
		ChannelItem root = TreeUtils.foreach(new Channel(), list);

        URLRewriteConfig urlConfig =  URLRewriteConfig.getInstance();
        String pageSufffix = urlConfig.getPageSuffix();// 页面后缀
		

		StringBuilder str = new StringBuilder();
        StringBuilder strSub = new StringBuilder();

		String web = (String) ActionContext.getReq().getAttribute(AppStatic.WEB_APP_URL);

		// 一级菜单
		str.append("<div class=\"nav\">\n");
        str.append("	<div class=\"nav-first\">\n");
        str.append("		<ul class=\"layout\">\n");

        // 二级菜单
        strSub.append("  <div class=\"subnav\">\n" +
                "      <div class=\"layout\">\n");

		List<ChannelItem> items = root.getChild();
		Iterator<ChannelItem> it = items.iterator();
		while(it.hasNext()){
			ChannelItem item = it.next();
			Channel c = item.getChannel();
			
			str.append("  <li><a ");
            if(!"dept".equals(c.getUrl())){
                str.append("href=\"").append(web).append("/").append(c.getUrl()).append(pageSufffix).append("\">");
            }else{
                str.append(" href=\"javascript:;\">");
            }
            if(!StringUtils.isEmpty(c.getLangkey())){
                str.append(languageModel.get(c.getLangkey()));
            }else{
                str.append(c.getName());
            }

            str.append("</a></li>");


            // 二级菜单拼接


            if("dept".equals(c.getUrl())){// 科室导航
                strSub.append("    <div class=\"subnav-item daohang\">\n");

                CategoryService categoryService = SpringContextHolder.getBean(Services.CATEGORY);


                CategoryItem deptRoot = categoryService.getAllTree();


                List<CategoryItem> categorys = deptRoot.getChild();
                Iterator<CategoryItem> it2 = categorys.iterator();
                while(it2.hasNext()) {
                    CategoryItem categoryItem = it2.next();
                    Category category = categoryItem.getNode();

                    int count = 1;
                    strSub.append("<p class=\"fl\">\n");
                    strSub.append("   <b>"+category.getName()+"</b>");
                    List<CategoryItem> categoryItemList = categoryItem.getChild();
                    Iterator<CategoryItem> ccit = categoryItemList.iterator();
                    while(ccit.hasNext()){
                        CategoryItem citem = ccit.next();
                        Category cc = citem.getNode();

                        strSub.append("     <a href=\"").append(web).append("/category/").append(cc.getId()).append(pageSufffix).append("\">");
//                        if(!StringUtils.isEmpty(cc.getLangkey())){
//                            strSub.append(languageModel.get(cc.getLangkey()));
//                        }else{
                            strSub.append(cc.getName());
//                        }
                        strSub.append("</a>\n");
//                        if(count%5 == 0){
//                            strSub.append("</p>");
//                            strSub.append("<p class=\"fl\">\n");
//                        }
                        count++;
                    }
                    strSub.append("</p>");
                }
                strSub.append("    </div>\n");



            }else{// 其他导航
                strSub.append("    <div class=\"subnav-item\">\n");
                List<ChannelItem> citems = item.getChild();
                Iterator<ChannelItem> cit = citems.iterator();
                while(cit.hasNext()){
                    ChannelItem citem = cit.next();
                    Channel cc = citem.getChannel();
                    strSub.append("     <a href=\"").append(web).append("/").append(cc.getUrl()).append(pageSufffix).append("\">");
                    if(!StringUtils.isEmpty(cc.getLangkey())){
                        strSub.append(languageModel.get(cc.getLangkey()));
                    }else{
                        strSub.append(cc.getName());
                    }
                    strSub.append("</a>\n");
                }
                strSub.append("    </div>\n");

            }
		}
		str.append("        </ul>\n");
        str.append("    </div>\n");


        // 对接二级菜单

        strSub.append("        </div>\n");
        strSub.append("    </div>\n");
        strSub.append("</div>\n");


        str.append(strSub);






		 
		env.getOut().write(str.toString());
	}
	
}
