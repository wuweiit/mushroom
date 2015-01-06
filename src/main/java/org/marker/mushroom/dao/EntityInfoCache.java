package org.marker.mushroom.dao;

import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.dao.annotation.Entity;

public class EntityInfoCache {

	public final Map<Class,MapConfig> data = new HashMap<Class,MapConfig>();
	
	
	public boolean containsKey(String key){
		return data.containsKey(key);
	}


	public MapConfig getMapConfig(Class<? extends Object> clzz) throws Exception {
		if(data.containsKey(clzz)){
			return (MapConfig) data.get(clzz);
		}
		
		
		String tableName, primaryKey;
		
		Entity entityConfig = clzz.getAnnotation(Entity.class);
		if(entityConfig != null){
			tableName  = entityConfig.value();
			primaryKey = entityConfig.key();
		}else{
			throw new Exception(clzz + " not has Annotation @Entity!");
		} 
		MapConfig config = new MapConfig(tableName, primaryKey);
		
		 
		
		
		data.put(clzz, config);
		
		return config;
	}
}
