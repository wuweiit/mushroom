package org.marker.mushroom.core.channel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 导航项
 *
 * @author marker
 *
 * Created by marker on 2017/6/24.
 */
public class NavItem implements Serializable{

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public NavItem getParent() {
        return parent;
    }

    public void setParent(NavItem parent) {
        this.parent = parent;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NavItem> getChild() {
        return child;
    }

    public void setChild(List<NavItem> child) {
        this.child = child;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
