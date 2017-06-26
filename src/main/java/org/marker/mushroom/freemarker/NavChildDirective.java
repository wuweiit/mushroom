package org.marker.mushroom.freemarker;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.channel.NavItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 通用二级导航功能实现
 *
 * 调用代码： <@NavChild items="navList"></@NavChild>
 * 
 * @author marker
 * @version 1.0
 */
public class NavChildDirective implements TemplateDirectiveModel{



	private IChannelDao channelDao;


	public NavChildDirective() {

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

        WebParam webParam = WebParam.get();
        int level = StringUtils.countMatches(webParam.pageName,"/") + 1;// 判断当前为几级栏目


        Channel channel = new Channel();
        // 当前一级页面
        if(level == 1){
            channel.setId(webParam.channel.getId());
            // TODO 此处有冗余查询待优化
            NavItem root = TreeUtils.build(channel, channelList, activeId);
            env.setVariable(itemsName, getBeansWrapper().wrap(root.getChild()));
        }else if(level == 2){// 当前二级页面
            channel.setId(webParam.channel.getPid());
            // TODO 此处有冗余查询待优化
            NavItem root = TreeUtils.build(channel, channelList, activeId);
            env.setVariable(itemsName, getBeansWrapper().wrap(root.getChild()));

        }else{
            // TODO 占时不实现三级页面输出。

        }

        body.render(env.getOut());
	}

    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                new BeansWrapper();
        return beansWrapper;
    }
	
}
