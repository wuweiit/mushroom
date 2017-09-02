package org.marker.mushroom.core.channel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.utils.StringUtil;
import org.marker.urlrewrite.URLRewriteEngine;


/**
 * 栏目树
 * (提供将数据库数据转换为栏目树功能)
 *
 *
 * @author marker
 * */
public class TreeUtils {

	
	public static ChannelItem foreach(Channel currentChannel, List<Channel> list){
		ChannelItem item = new ChannelItem();
		item.setChannel(currentChannel);
		for(Channel c : list){
			if(c.getPid() == currentChannel.getId()){//如果是当前栏目的子栏目
				ChannelItem item2 = foreach(c,list);
				item.child.add(item2);
			}
		}
		return item;
	}


    /**
     * 分类构建树
     * @param current
     * @param list
     * @return
     */
    public static CategoryItem foreach(Category current, List<Category> list){
        CategoryItem item = new CategoryItem();
        item.setNode(current);
        for(Category c : list){
            if(c.getPid() == current.getId()){//如果是当前栏目的子栏目
                CategoryItem item2 = foreach(c,list);
                item.child.add(item2);
            }
        }
        return item;
    }


    /**
     * 构建导航树
     * @param channelList
     * @param activeId
     * @return
     */
    public static NavItem build(Channel channel, List<Channel> channelList, Integer activeId) {
        NavItem item = new NavItem();
        item.setName(channel.getName());
        item.setSort(channel.getSort());
        item.setId(channel.getId());
        item.setPid(channel.getPid());


        // 获取URL重写实例
        URLRewriteEngine urlrewrite = SingletonProxyFrontURLRewrite.getInstance();


        // 手动制定的跳转地址（避免闪屏，输出跳转目标）
        String redirect = channel.getRedirect();
        if(!StringUtils.isEmpty(redirect)){
            if(redirect.startsWith("http")){
                String url = urlrewrite.encoder("/cms?p="+channel.getUrl());
                item.setUrl(url);
            }else{
                String url = urlrewrite.encoder("/cms?p="+redirect);
                item.setUrl(url);
            }
        }else{
            String url = urlrewrite.encoder("/cms?p="+channel.getUrl());
            item.setUrl(url);
        }
        Iterator<Channel> it = channelList.iterator();
        while(it.hasNext()){
            Channel c = it.next();
            if(c.getPid() == channel.getId()){// 如果是当前栏目的子栏目
                NavItem item2 = build(c, channelList, activeId);
                item.addChild(item2);

                // 设置活跃
                if(activeId == c.getId()){
                    item2.setActive(true);// 设置活跃后，自动设置父节点活跃
                }

            }
        }

        // 设置活跃
        if(activeId == channel.getId()){
            item.setActive(true);// 设置活跃后，自动设置父节点活跃
        }

        return item;
    }


    /**
     * 计算子栏目
     * @param channelList
     * @param cidList
     * @param cid
     */
    public static void buildChildIdList(List<Channel> channelList, List<Integer> cidList, int cid) {
        for(Channel channel: channelList){
            if(channel.getPid() == cid){
                cidList.add(channel.getId());
                buildChildIdList(channelList,cidList,channel.getId());
            }
        }
    }


    /**
     * 处理模拟树结构
     * （最多仅支持4级递归）
     *
     * @param list 模拟树
     * @param parentId 父级ID
     * @param level 级别（1，2，3）
     */
    public static void processSimulateTree(List<Map<String, Object>> list, int parentId, int level) {

        if(level > 3){ // 限制级数
            return;
        }

        // 计算同级节点
        Iterator<Map<String, Object>> it2 = list.iterator();
        List<JSONObject> brotherList = new ArrayList<>();
        while(it2.hasNext()){
            Map<String, Object> bean = it2.next();
            JSONObject jsonObject = new JSONObject(bean);
            int pid = jsonObject.getIntValue("pid");
            if(parentId == pid){
                brotherList.add(jsonObject);
            }
        }
        // 兄弟节点结束点
        if(brotherList.size() == 0){
            return;
        }
        JSONObject endElement = brotherList.get(brotherList.size()-1);

        int endId = endElement.getIntValue("id");




        Iterator<JSONObject> it = brotherList.iterator();
        while(it.hasNext()){
            JSONObject jsonObject = it.next();
            int id = jsonObject.getIntValue("id");

            // 计算空格数量
            jsonObject.put("space", StringUtil.buidString("&nbsp;&nbsp;&nbsp;",level));

            // 计算是否结尾
            jsonObject.put("end", endId == id);

            processSimulateTree(list, id, level + 1);
        }
    }
}
