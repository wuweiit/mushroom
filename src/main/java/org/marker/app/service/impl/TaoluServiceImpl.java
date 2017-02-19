package org.marker.app.service.impl;

import org.marker.app.service.TaoluService;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.ArticleTaolu;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.dao.annotation.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ROOT on 2016/11/26.
 */
@Service
public class TaoluServiceImpl implements TaoluService {


    @Autowired
    private ISupportDao commonDao;


    /** 数据库配置 */
    protected static final DataBaseConfig dbConfig = DataBaseConfig.getInstance();


    public Map<String, Object> get(int id) {
        Map articleTaolu = commonDao.findById(ArticleTaolu.class, id);
        String prefix = dbConfig.getPrefix();// 表前缀
        String tableName  = ArticleTaolu.class.getAnnotation(Entity.class).value();
        // 添加浏览量
        commonDao.update("update "+prefix+tableName+" set views = views +1 where id = ?",id);

        return articleTaolu;
    }

    @Override
    public List<Map<String, Object>> pullData(int endId, int drection,int size, String keyword) {



        String sql = "select a.id,a.icon,a.keywords,a.description,a.author,a.title,a.time, a.views,a.category from mr_taolu a where 1=1 and ";
        List list = new ArrayList<>(4);
        if(endId == 0){
            sql +=" 1=1 order by a.time asc limit ?";
            list.add(size);
        }else{
            if(drection > 0){// 上啦
                sql += "a.id < ?";
            }else{// 下拉
                sql += "a.id > ?";
            }
            list.add(endId);
            sql +=" order by a.time asc limit ?";
            list.add(size);
        }




        return commonDao.queryForList(sql,list.toArray());
    }
}
