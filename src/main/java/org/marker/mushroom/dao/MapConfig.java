package org.marker.mushroom.dao;

public class MapConfig {

	private String tableName;
	private String primaryKey;
	
	
	
	public MapConfig(String tableName, String primaryKey) {
		super();
		this.tableName = tableName;
		this.primaryKey = primaryKey;
	}
	
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
	
}
