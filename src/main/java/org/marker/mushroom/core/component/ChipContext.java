package org.marker.mushroom.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.core.IChip;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ISupportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 碎片
 * */
@Service(SystemStatic.SYSTEM_CMS_CHIP)
public class ChipContext implements IChip{

	private static final Log log = LogFactory.getLog(ChipContext.class);
	
	@Autowired ISupportDao commonDao;
	 
	private boolean isSyn = false;
	
	private HashMap<String, Object> data;
	
	
	 
	public synchronized void syn(){
		if(!isSyn){
			String prefix = DataBaseConfig.getInstance().getPrefix();
			List<Map<String, Object>> list = commonDao.queryForList("select * from "+prefix+"chip");
			data = new HashMap<String, Object>();
			for(Map<String, Object> o: list){
				String mark = o.get("mark").toString();
				data.put(mark, o.get("content"));
			}
			log.info("syn chip data ");
			isSyn = false;
		}
	}
 
	public Object getVector() {
		syn();
		return data;
	}
}


