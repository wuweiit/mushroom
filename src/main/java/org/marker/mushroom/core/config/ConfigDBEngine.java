package org.marker.mushroom.core.config;

import org.marker.mushroom.core.config.annotation.IgnoreCopyProperties;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.utils.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

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
public abstract class ConfigDBEngine<S extends ConfigDBEngine> implements InitializingBean {

	/** 日志记录器 */
	protected static Logger logger = LoggerFactory.getLogger(ConfigDBEngine.class);


	/**
	 * 配置信息存放对象
	 *(API: Properties类是线程安全的：多个线程可以共享单个Properties对象而无需进行外部同步)
	 * */
	@IgnoreCopyProperties
	protected Properties properties = new Properties();

	/** 配置文件编码集UTF-8 */
	public static final String FILE_ENCODEING = "utf-8";

	/**
	 * 初始化就读取配置文件哦
	 */
	public ConfigDBEngine() {
	}

	@Override
	public void afterPropertiesSet() {
		logger.debug("[{}] load db config", this.getClass().getSimpleName());
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
		JdbcTemplate jdbcTemplate = SpringContextHolder.getApplicationContext().getBean(JdbcTemplate.class);
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
			properties.put(key, value == null?"":value);
		}
		BeanUtils.copyProperties(properties, this);



	}


	/**
	 * 异步存储配置信息到数据库
	 * @return
	 */
	public void storeAsync() {
		logger.info("ConfigDBEngine store async...");

		logger.debug("ConfigDBEngine copy bean to this.properties...");
		Properties properties = ArrayUtils.beanToPropertiesConverter(this);
//		this.properties.putAll(properties);

		logger.debug("ConfigDBEngine copy properties to [{}].properties...",this.getClass().getSimpleName());
		ConfigDBEngine configDBEngine = SpringContextHolder.getApplicationContext().getBean(this.getClass());
		configDBEngine.properties.putAll(properties);

		configDBEngine.store();
		logger.info("ConfigDBEngine store async end.");

	}



	/**
	 * 配置持久化
	 */
	public void store(){
		String name = this.getClass().getSimpleName();

        DataBaseConfig dbcfg = DataBaseConfig.getInstance();
        String prefix = dbcfg.getPrefix();



        String sql = "update "+prefix+"sys_config set value=? where config=? and `key`=?";
		List<Object[]> paramsList = new ArrayList<>(this.properties.entrySet().size());
		Iterator<Map.Entry<Object, Object>> it = this.properties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			paramsList.add(new Object[]{value, name, key});
		}
		JdbcTemplate jdbcTemplate = SpringContextHolder.getApplicationContext().getBean(JdbcTemplate.class);
		jdbcTemplate.batchUpdate(sql, paramsList);

	}
//
//    public void loadFile(String file ) {
//
//	    File _cfgFile = new File(file);
//        FileInputStream in = null;
//        InputStreamReader isr = null;
//        try {
//            in = new FileInputStream(_cfgFile);
//            isr = new InputStreamReader(in, FILE_ENCODEING);
//            this.properties.load(isr);//读取配置文件
//        } catch (IOException e) {
//            logger.error("IOException " , e);
//        }finally{
//            try {
//                if (isr != null) {
//                    isr.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//            }
//        }
//
//    }


//	/**
//	 * 配置持久化
//	 */
//	public void storeFile(File cfgFile){
//		OutputStream out = null;
//		OutputStreamWriter osw = null;
//		try{
//			out = new FileOutputStream(cfgFile);
//			osw = new OutputStreamWriter(out, FILE_ENCODEING);
//			this.properties.store(osw, "");
//
//		}catch (FileNotFoundException e) {
//			logger.error("config file not found " + cfgFile.getAbsolutePath(), e);
//		} catch (UnsupportedEncodingException e) {
//			logger.error("not supported encoding " + FILE_ENCODEING,e);
//		} catch (IOException e) {
//			logger.error("IOException " + cfgFile.getAbsolutePath(), e);
//		}finally{
//			try {
//				if(osw != null){
//					osw.close();
//				}
//				if(out != null){
//					out.close();
//				}
//			} catch (IOException e) {
//				logger.error(
//						"close stream IOException "
//								+ cfgFile.getAbsolutePath(), e);
//			}
//		}
//	}
}



