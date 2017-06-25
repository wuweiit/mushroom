package org.marker.mushroom.core.channel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 *
 * Created by marker on 2017/6/24.
 */
@Data
public class NavItem {

    private Integer id = 0;

    private Integer pid = 0;
    /** 父节点 */
    private NavItem parent;

    private int sort;

    private String name;

    private String url;

    /** 子节点*/
    private List<NavItem> child = new ArrayList<>(5);

    private boolean active;// 当前活动的栏目

    private boolean end; // 是否是结束标记


    /**
     * 是否包含子节点
     * @return
     */
    private boolean hasChild(){
        if(child != null && child.size()>0){
            return true;
        }
        return false;
    }


    /**
     * 添加子节点
     * @param item
     */
    public void addChild(NavItem item) {
        if(child == null){
            child = new ArrayList<>(5);
        }
        child.add(item);// 添加子节点
        item.setParent(this);// 设置父节点
    }


    /**
     * 设置活跃树的路径
     * @param active
     */
    public void setActive(boolean active){
        this.active = active;
        foreachActive(this, active);
    }

    /**
     * 父节点遍历设置活跃项
     *
     * @param item 当前节点
     * @param active 活跃状态
     */
    private void foreachActive(NavItem item, boolean active) {
        if(item == null) return;
        NavItem parent = item.getParent();
        if(parent != null){
            parent.setActive(active);
            foreachActive(parent.getParent(), active);
        }
    }

    public String toString(){
        return "overwrite";
    }
}
