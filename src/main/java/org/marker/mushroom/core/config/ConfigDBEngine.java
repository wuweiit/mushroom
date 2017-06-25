package org.marker.mushroom.core.config;

import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.holder.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.util.*;


/**
 * 抽象配置引擎类
 * 
 * 说明：此类用于开发配置对象，可以动态的改变配置信息以及持久化，保证了系统动态性。
 * 实现：采用JDK内置的Properties实现，线程安全。
 *
 *
 *
 * 2013-12-02 经过(500次/秒)并发测试，没有出现问题。
 *
 * @update 20170617 marker 持久化采用数据库。
 * 
 * @time 2013-11-15
 * @author marker
 * */
public abstract class ConfigDBEngine {

	/** 日志记录器 */
	protected Logger logger = LoggerFactory.getLogger(ConfigDBEngine.class);


	/**
	 * 配置信息存放对象
	 *(API: Properties类是线程安全的：多个线程可以共享单个Properties对象而无需进行外部同步)
	 * */
	protected Properties properties = new Properties();

	/** 配置文件编码集UTF-8 */
	public static final String FILE_ENCODEING = "utf-8";


	/** 带注入 */
	private JdbcTemplate jdbcTemplate;


	/**
	 * 初始化就读取配置文件哦
	 */
	public ConfigDBEngine(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.read();
	}


	/**
	 * 获取配置字符串
	 * @param key 键
	 * @return String 
	 */
	public String get(String key) {
		return this.properties.getProperty(key);
	}

	
	/**
	 * 设置配置key = val
	 * @param key 键
	 * @param val 值
	 */
	public void set(String key, String val) {
		this.properties.put(key, val);
	}

	

	public Properties getProperties() {
		return this.properties;
	}

	


	/**
	 * 读取配置文件
	 * 线程安全
	 */
	public synchronized void read(){
		String name = this.getClass().getSimpleName();

        DataBaseConfig dbcfg = DataBaseConfig.getInstance();
        String prefix = dbcfg.getPrefix();


		String sql = "select * from "+prefix+"sys_config where config=?";
		List<Map<String,Object>> list =  jdbcTemplate.queryForList(sql, name);
		Iterator<Map<String,Object>> it = list.iterator();

		while(it.hasNext()){
			Map map = it.next();
			String key = (String)map.get("key");
			String value = (String)map.get("value");
			properties.put(key,value);
		}
	}
	
	
	
	/**
	 * 配置持久化
	 */
	public void store(){
		String name = this.getClass().getSimpleName();

        DataBaseConfig dbcfg = DataBaseConfig.getInstance();
        String prefix = dbcfg.getPrefix();



        String sql = "update "+prefix+"sys_config set value=? where config=? and `key`=?";
        String sqlInsert = "insert into "+prefix+"sys_config values(null,?,?,?)";

        String sqlExist = "select count(1) from "+prefix+"sys_config where config=? and `key`=?";

		Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			boolean status = jdbcTemplate.queryForObject(sqlExist, Boolean.class,name, key);
			if(status){
                jdbcTemplate.update(sql, value,name,key);
            }else{
                jdbcTemplate.update(sqlInsert,name,key,value);
            }

		}
	}

    public void loadFile(String file ) {

	    File _cfgFile = new File(file);
        FileInputStream in = null;
        InputStreamReader isr = null;
        try {
            in = new FileInputStream(_cfgFile);
            isr = new InputStreamReader(in, FILE_ENCODEING);
            this.properties.load(isr);//读取配置文件
        } catch (IOException e) {
            logger.error("IOException " , e);
        }finally{
            try {
                if (isr != null) {
                    isr.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }

    }
}



