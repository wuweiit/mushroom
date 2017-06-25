package org.marker.mushroom.core.channel;

import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;

import java.util.ArrayList;
import java.util.List;


/**
 * 树结构的栏目Item节点
 * @author marker
 * */
public class CategoryItem {

	/** 此节点的栏目对象 */
	private Category node;
	
	/** 此节点的子栏目对象集合 */
	public List<CategoryItem> child = new ArrayList<CategoryItem>(5);
	
	
	public String getChildIdToString(){
		StringBuilder sb = new StringBuilder(node.getId()+",");
		heihei(node.getId(),sb, this.child);
		return sb.toString().substring(0,sb.length()-1);
	}
	
	
	public boolean hasChild(){
		return child.size() == 0?false:true;
	}
	
	private StringBuilder heihei(long id,StringBuilder str, List<CategoryItem> child){
		for(CategoryItem i : child){
			str.append(i.node.getId()+",");
			if(i.hasChild()){//如果有子节点
				heihei(i.node.getId(),str,i.child);
			}
		}
		return str;
	}


	public Category getNode() {
		return node;
	}


	public void setNode(Category node) {
		this.node = node;
	}


	public List<CategoryItem> getChild() {
		return child;
	}


	public void setChild(List<CategoryItem> child) {
		this.child = child;
	}

	
	
}
