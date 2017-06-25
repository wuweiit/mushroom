package org.marker.mushroom.freemarker;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.marker.develop.freemarker.MessageWrapperModel;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.NavItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.SendDataToView;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;


/**
 * 通用导航功能实现
 *
 * 将栏目数据通过变量navList 输出。
 * 支持当前活跃栏目
 *
 * 
 * 调用代码： <@Nav></@Nav>
 * 
 * @author marker
 * @version 1.0
 */
public class NavDirective implements TemplateDirectiveModel{



	private IChannelDao channelDao;


	public NavDirective() {

	    channelDao = SpringContextHolder.getBean(DAO.CHANNEL);
	}
	
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map  params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
        String itemsName = "navList" ;
        Object itemsObject = params.get("items");
        if(itemsObject != null){
            itemsName = ((SimpleScalar)itemsObject).getAsString();
        }

        List<Channel> channelList = channelDao.findValid();

        WebParam param = WebParam.get();
        Integer activeId = param.channel.getId();// 当前活动的栏目ID


        NavItem root = TreeUtils.build(new Channel(), channelList, activeId);

        env.setVariable(itemsName, getBeansWrapper().wrap(root.getChild()));
        body.render(env.getOut());
	}

    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                new BeansWrapper();
        return beansWrapper;
    }
	
}
