/**
 * 
 */
package org.marker.mushroom.statistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ISupportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 访问统计工具
 * @author marker
 * @date 2013-9-6 下午3:24:10
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Service("statisticUtil")
public class StatisticUtil {

	// 通用Dao
	@Autowired ISupportDao commonDao;
	
	
	private static List<String> list = Collections.synchronizedList(new ArrayList<String>(30));
	
	private String prefix = DataBaseConfig.getInstance().getPrefix();
	
	
	
	public synchronized void visited(String info){
		if(list.size() < 5-1){
			list.add(info);
		}else{//满了，就批量推入数据库
			list.add(info);
			List<Object[]> visited_data = new ArrayList<Object[]>();
			Iterator<String> it = list.iterator();
			while(it.hasNext()){
				String currentStr = it.next();
				visited_data.add(currentStr.split(",")); 
			} 
			commonDao.batchUpdate("insert into "+prefix+"visited_his(ip,time) values(?,sysdate())", visited_data); 
			list.clear();
		} 
	}
	
	
}
