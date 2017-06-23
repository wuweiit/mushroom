package org.marker.mushroom.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryExplorerTree;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.channel.CategoryItem;
import org.marker.mushroom.core.channel.ChannelItem;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.dao.ICategoryDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 分类业务层处理
 * @author marker
 * @version 1.0
 */
@Service(Services.CATEGORY)
public class CategoryService extends BaseService {

	@Autowired private ISupportDao commonDao;
	
	@Autowired private ICategoryDao categoryDao;
	
	
	/**
	 * 查询所有分类信息
	 * @return List<Map<String, Object>>
	 */
	public List<Category> list() {
		return categoryDao.list();
	}

	/**
	 * 查询模型的分类信息
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> list(String modelType) {
		String sql = "select * from "+config.getPrefix()+"category where model=?";
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

	
	
	
	/**
	 * 查询子Id集合字符串
	 * 方便使用where语句
	 * 
	 * @param cid
	 * @return
	 */
	public String findChildIds(int cid) {
		List<Category> list = categoryDao.list();
		StringBuilder sb = new StringBuilder();
		sb.append(""+cid);
		fetch(cid, sb, list);
		return sb.toString();
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
		return categoryDao.findById(Category.class,id);
	}

    public Object getAll() {
        return categoryDao.findAll(Category.class);
    }


    /**
     * 获取分类树
     * @return
     */
    public CategoryItem getAllTree() {
        List<Category> list = categoryDao.findAll();
        return TreeUtils.foreach(new Category(), list);
    }

    public Object getUserGroupCategory(int userGroupId) {
        if(Core.ADMINI_GROUP_ID == userGroupId){
            return categoryDao.findAll();
        }else{
            return categoryDao.findByGroupId(userGroupId);
        }

    }
}
