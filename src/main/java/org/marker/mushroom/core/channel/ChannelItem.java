package org.marker.mushroom.core.channel;

import java.util.ArrayList;
import java.util.List;

import org.marker.mushroom.beans.Channel;


/**
 * 树结构的栏目Item节点
 * @author marker
 * */
public class ChannelItem {

	/** 此节点的栏目对象 */
	private Channel channel; 
	
	/** 此节点的子栏目对象集合 */
	public List<ChannelItem> child = new ArrayList<ChannelItem>(5);
	
	
	public String getChildIdToString(){
		StringBuilder sb = new StringBuilder(channel.getId()+",");
		heihei(channel.getId(),sb, this.child);
		return sb.toString().substring(0,sb.length()-1);
	}
	
	
	public boolean hasChild(){
		return child.size() == 0?false:true;
	}
	
	private StringBuilder heihei(long id,StringBuilder str, List<ChannelItem> child){
		for(ChannelItem i : child){
			str.append(i.channel.getId()+",");
			if(i.hasChild()){//如果有子节点
				heihei(i.channel.getId(),str,i.child);
			}
		}
		return str;
	}


	public Channel getChannel() {
		return channel;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}


	public List<ChannelItem> getChild() {
		return child;
	}


	public void setChild(List<ChannelItem> child) {
		this.child = child;
	}

	
	
}
