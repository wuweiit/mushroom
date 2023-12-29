package org.marker.mushroom.service.impl;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.core.channel.CategoryItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.dao.ISiteDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 站点业务层处理
 * @author marker
 * @version 1.0
 */
@Service(Services.SITE)
public class SiteService extends BaseService {

	@Resource
	private ISupportDao commonDao;
	
	@Resource
	private ISiteDao siteDao;
	
	
	/**
	 * 查询所有分类信息
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> list() {
		String sql = "select * from "+config.getPrefix()+"site ";
		return commonDao.queryForList(sql, null);
	}

	/**
	 * 查询模型的分类信息
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> list(String modelType) {
		String sql = "select * from "+config.getPrefix()+"site where model=?";
		return commonDao.queryForList(sql, modelType);
	}

	
	
	// 通过分类ID查询模型类型
	public String findModelById(Serializable id){
		String sql = "select model from "+config.getPrefix()+"category where id=?"; 
		return commonDao.queryForObject(sql, String.class, id);
	}

 
	
	/**
	 * 判断分类节点是否有子节点
	 * @param id
	 * @return
	 */
	public boolean hasChild(Serializable id) { 
		String sql = "select count(id) from "+config.getPrefix()+"category where pid=?"; 
		int a = commonDao.queryForObject(sql, Integer.class, id);  
		return a > 0? true : false;
	}

	
	


	
	
	// 抓取数据
	private void fetch(int cid, StringBuilder sb, List<Category> list) {
		for(Category c : list){
			if(c.getPid() == cid){
				sb.append(",").append(c.getId());
				fetch(c.getId(), sb, list);
			}
		}
	}



	public Map<String,Object> get(int id) {
		return siteDao.findById(Category.class,id);
	}

    public Object getAll() {
        return siteDao.findAll(Category.class);
    }


    /**
     * 获取分类树
     * @return
     */
    public CategoryItem getAllTree() {
        List<Category> list = siteDao.findAll();
        return TreeUtils.foreach(new Category(), list);
    }

    public Object getUserGroupCategory(int userGroupId) {
        if(Core.ADMINI_GROUP_ID == userGroupId){
            return siteDao.findAll();
        }else{
            return siteDao.findByGroupId(userGroupId);
        }

    }
}
