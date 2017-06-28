package org.marker.mushroom.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.annotation.Entity;
import org.marker.mushroom.dao.annotation.EntityFieldIgnore;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.utils.SQLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



/**
 * 数据库操作对象引擎
 * 实现：基于Spring的JDBCTemplate开发的通用对象。
 * 目的：简化数据库代码
 * 最新采用slf4j日志接口
 * @author marker
 * @date 2014-08-10
 * @version 1.0 
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public abstract class DaoEngine implements ISupportDao {
	
	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.DAOENGINE);
	
	/** 数据库配置 */
//	protected static final DataBaseConfig dbConfig = DataBaseConfig.getInstance(); 
	
	
	
	/** 自动注入jdbc模板操作 */
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	
	/** 基本信息缓存  */
	private static final EntityInfoCache cache = new EntityInfoCache();
	
	
	
	
	
	/**
	 * 泛型构造方法目的是为了简化实现
	 */
	public DaoEngine() {  }
	
	
	/* 
	 * ==================================================================
	 *                       数据查询接口实现
	 * ==================================================================
	 */
	
	// 查询单个对象实现
	@Override
	public Map<String,Object> queryForMap(String sql, Object... args){
		return jdbcTemplate.queryForMap(sql, args);
	}
	
	
	// 查询对象集合实现
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return jdbcTemplate.queryForList(sql, args);
	}
	
	// 查询对象 
	@Override
	public <T> T queryForObject(String sql, Class<T> clzz, Object... args) {
		return jdbcTemplate.queryForObject(sql, clzz, args);
	}

	// 查询是否存在
	public boolean exists(String sql, Object... args){
		int num = jdbcTemplate.queryForObject(sql, Integer.class, args);
		return num > 0? true : false;
	}
 
	
	
	/* 
	 * ==================================================================
	 *                       数据更新接口实现
	 * ==================================================================
	 */
	
	// 批量更新
	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	// 更新数据
	@Override
	public boolean update(String sql, Object... args) {
		return jdbcTemplate.update(sql, args) > 0? true:false; 
	}
	
	
	
	
	
	/* 
	 * ==================================================================
	 *                       数据删除接口实现
	 * ==================================================================
	 */
	
	// 批量删除
	@Override
	public boolean deleteByIds(Class<?> clzz, String ids) {
		String prefix = getPreFix();// 表前缀
		
		MapConfig map = null;
		try {
			map = cache.getMapConfig(clzz);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(prefix).append(map.getTableName())
				.append(" where ").append(map.getPrimaryKey()).append(" in(")
				.append(ids).append(")");
		return jdbcTemplate.update(sql.toString()) > 0 ? true : false;
	}
	

	
	
	// 批量删除
	@Override
	public boolean deleteByIds(String tableName, String key, String ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(tableName).append(" where ")
				.append(key).append(" in(").append(ids).append(")");
		return jdbcTemplate.update(sql.toString()) > 0 ? true : false;
	}
	
	
	
	

	/* =========================================
	 *                 根据ID查询
	 * ========================================= */
	@Override
	public Map<String, Object> findById(Class<?> clzz, Serializable id ) {
		String prefix = getPreFix();// 表前缀 
		String tableName  = clzz.getAnnotation(Entity.class).value();
		String primaryKey = clzz.getAnnotation(Entity.class).key();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(prefix).append(tableName)
				.append(" where ").append(primaryKey).append("=?");
		return queryForMapNoException(sql.toString(), id);
	} 

	// 无异常查询
	private Map<String, Object> queryForMapNoException(String sql,
			Object... args) {
		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (Exception e) { }
		return new HashMap<String, Object>();
	}

	
	// 分页查询
	@Override
	public Page findByPage(int currentPageNo, int pageSize, String sql,
			Object... args) {

		int beginPos = sql.toLowerCase().indexOf("from");
		String hql4Count = "select count(*) " + sql.substring(beginPos);
		sql += " limit " + (currentPageNo - 1) * pageSize + "," + pageSize;

		// 获取总条数
		int totalRows = jdbcTemplate.queryForObject(hql4Count, Integer.class, args);
		 
		List<Map<String, Object>> data = queryForList(sql, args);
	 
		Page page = new Page(currentPageNo,totalRows, pageSize);
		page.setData(data);// 获取数据集合 
		return page;
	}

	// 保存对象  
	public boolean save(Object entity) {
		String prefix = getPreFix();
		Class<?> clzz = entity.getClass();
	

		
		MapConfig map = null;
		try {
			map = cache.getMapConfig(clzz);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		
		
		
		// 构造SQL字符串
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(prefix).append(map.getTableName()).append("(");
		StringBuilder val = new StringBuilder(" values(");
		Field[] fields = clzz.getDeclaredFields();
		int length = fields.length;
		final List<Object> list = new ArrayList<Object>(length);
		for (int i = 0; i < length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
//			if (fieldName.equals(map.getPrimaryKey())) {// 如果是主键
//				if(){
//
//				}
//
//				continue;
//			}
			if ("serialVersionUID".equals(fieldName)) {
				continue;
			}

            if(field.getAnnotation(EntityFieldIgnore.class) != null){
			    continue;
            }

			String methodName = "get"
					+ fieldName.replaceFirst(fieldName.charAt(0) + "",
							(char) (fieldName.charAt(0) - 32) + "");

			Method me = null;
			try {
				me = clzz.getMethod(methodName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			Object returnObject = null;
			try {
				returnObject = me.invoke(entity);
			} catch (IllegalArgumentException e) { 
				e.printStackTrace();
			} catch (IllegalAccessException e) { 
				e.printStackTrace();
			} catch (InvocationTargetException e) { 
				e.printStackTrace();
			}//
			if (returnObject != null) {// 如果返回值为null
				sql.append("`" + fieldName + "`");
				val.append("?");
				list.add(returnObject); 
					sql.append(",");
					val.append(","); 
			}
		}
		 
		final StringBuilder sql2 = new StringBuilder(sql.substring(0, sql.length()-1));
		StringBuilder val2 = new StringBuilder(val.substring(0, val.length()-1));
	
		
		sql2.append(")").append(val2).append(")"); 
		KeyHolder holder = new GeneratedKeyHolder();

		
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql2.toString()); 
				Object[] params = list.toArray();
				for(int i=0; i<params.length;i++){
					ps.setObject(i+1, params[i]);
				} 
				return ps;
			}
		}, holder);
		
		
		
		try {
			Method me = clzz.getMethod("setId", Integer.class);
			me.invoke(entity, holder.getKey().intValue());
		} catch ( Exception e) { 
			e.printStackTrace();
		}
		
		return true;
		 
	}

	// 更新数据
	public boolean update(Object entity) {
		String prefix = getPreFix();
		Class<?> clzz = entity.getClass();
		Entity tableInfo = clzz.getAnnotation(Entity.class);
		String tableName = tableInfo.value();
		String primaryKey = tableInfo.key();

		StringBuffer sql = new StringBuffer();
		sql.append("update `").append(prefix).append(tableName)
				.append("` set ");
		List<Object> list = null;
		int id = 0;
		try {
			id = (Integer) clzz.getMethod("getId").invoke(entity);
			Field[] fields = clzz.getDeclaredFields();
			int length = fields.length;
			list = new ArrayList<Object>(length);
			for (int i = 0; i < length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if (fieldName.equals(primaryKey)) {// 如果是主键
					continue;
				}
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				}
                if(field.getAnnotation(EntityFieldIgnore.class) != null){
                    continue;
                }
				String methodName = "get"
						+ fieldName.replaceFirst(fieldName.charAt(0) + "",
								(char) (fieldName.charAt(0) - 32) + "");
				Method me = clzz.getMethod(methodName);
				Object returnObject = me.invoke(entity);
				if (returnObject != null) {// 如果返回值为null
					sql.append("`" + fieldName + "`").append("=?");
					list.add(returnObject); 
				    sql.append(","); 
				}
			} 
			sql = new StringBuffer(sql.substring(0, sql.length()-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.add(id);
		sql.append(" where `").append(primaryKey).append("`=?");
		  
		 
 
		return update(sql.toString(), list.toArray());
		 
	}

	// 查询某页数据
	@Override
	public List<Map<String, Object>> queryFotList(int currentPageNo,
			int pageSize, String sql, Object... args) {
		sql += " limit " + (currentPageNo - 1) * pageSize + "," + pageSize;
		return jdbcTemplate.queryForList(sql, args);
	}


    /**
     * 删除所有数据
     * @param clzz
     */
	public void deleteAll(Class<?> clzz){
		String prefix = getPreFix();
		Entity tableInfo = clzz.getAnnotation(Entity.class);
		String tableName = tableInfo.value();
		String sql = "delete from "+prefix+tableName;
		jdbcTemplate.execute(sql);
	}


	@Autowired
	PropertyPlaceholderConfigurer propertyPlaceholderConfigurer;



	// 前缀
	@Value("#{configProperties['mushroom.db.prefix']}")
	private String dbPrefix;



	/**
	 * 获取数据库前缀
	 * @return
	 */
	public String getPreFix(){
        return dbPrefix;
	}


	public List<Map<String,Object>> findAll(Class<?> clzz){
        String prefix = getPreFix();
        Entity tableInfo = clzz.getAnnotation(Entity.class);
        String tableName = tableInfo.value();
        String sql = "select * from " + prefix + tableName;
       return jdbcTemplate.queryForList(sql);
    }

}
