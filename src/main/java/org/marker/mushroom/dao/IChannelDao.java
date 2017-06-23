package org.marker.mushroom.dao;

import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;

/**
 * 栏目查询接口
 * @author marker
 * @date 2013-11-15 下午9:25:24
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public interface IChannelDao extends ISupportDao {

	Channel queryByUrl(String url);
	
	List<Channel> findAll();

	Channel queryByArticleId(Serializable cid);
	
	
	/**
	 * 查询可见栏目数据
	 * @return
	 */
	List<Channel> findValid();


	/**
	 * 查询排序最大的
	 * @param pid
	 * @return
	 */
	Channel findChildMaxSortMenuByPId(long pid);


	/**
	 * 更新同级end 为0
	 * @param pid
	 */
    void updateEnd0(long pid);


	/**
	 * 根据PId查询子菜单
	 * @param deptPid
	 * @return
	 */
	List<Channel> findByPid(int deptPid);

	public List<Channel> list();

    List<Channel> findByGroupId(int userGroupId);

    Channel findByUrl(String pageName);
}
