package org.marker.mushroom.ext.message;

import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.utils.FileTools;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.connection.convert.StringToRedisClientInfoConverter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


/**
 * 消息数据上下文对象
 * 
 * @author marker
 * @version 1.0
 */
public class MessageDBContext {
	
	/** 默认数据 */
	private Properties defaultData = new Properties();
	
	private Map<String, Properties> messageData = new HashMap<String, Properties>();

	List<String> updateKeys = new ArrayList<>();


	
	/** 语言列表 */
	private List<Locale> locales = new ArrayList<>(5);


	JdbcTemplate jdbcTemplate;

	public MessageDBContext(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate  = jdbcTemplate;
	}

	private static MessageDBContext messageDBContext;

	/** 是否初始化 */
	private static boolean init = false;


	/**
	 * 获取数据库配置实例
	 * */
	public static MessageDBContext getInstance(){
	    if(messageDBContext == null){
            JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
            messageDBContext = new MessageDBContext(jdbcTemplate);
             init = true;
        }
		return messageDBContext;
	}



	private boolean checkLocal(String lang){
	    for(Locale locale : locales){
            if(locale.equals(getLocal(lang))){
                return true;
            }
//	        String displayName = .getLanguage();
//	        if(displayName.equals(lang)){
//                return true;
//            }
        }
        return false;
    }

    private Locale getLocal(String lang){
        Locale locale = null;
        // 添加语言到集合
        String[] strs = lang.split("-");
        if(strs.length > 1){
            locale =new Locale(strs[0],strs[1]);
        }else{
            locale = new Locale(strs[0]);
        }
        return locale;
    }

	
	/**
	 * 初始化
     * （加入数据）
     *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException 
	 */
	public void init() throws Exception {

		DataBaseConfig dbcfg = DataBaseConfig.getInstance();
		String prefix = dbcfg.getPrefix();


		String sql = "select * from "+prefix+"sys_language";

	    List<Map<String, Object>> messageList = jdbcTemplate.queryForList(sql);

        Iterator<Map<String, Object>> it = messageList.iterator();
        while(it.hasNext()) {
            Map<String,Object> msg = it.next();

            String lang  = (String)msg.get("lang");
            String key   = (String)msg.get("key");
            String value = (String)msg.get("value");

            // 加载默认配置
            if("default".equals(lang)){
                defaultData.put(key, value);
                continue;
            }

            if(!checkLocal(lang)){
                create(lang);
            }

            Properties properties = messageData.get(lang);
            if(properties == null){
                properties = new Properties();
                messageData.put(lang, properties);
            }
            properties.put(key, value);

        }

	}
	
	
	
	/**
	 * 创建一个语言
	 * @param lang
	 * @throws Exception 
	 */
	public void create(String lang) throws Exception {
		if(this.messageData.containsKey(lang)){
			throw new Exception("this language is use!");
		}

		// 添加语言到集合
		String[] strs = lang.split("-");
		if(strs.length > 1){
            locales.add(new Locale(strs[0],strs[1]));
		}else{
            locales.add(new Locale(strs[0]));
		}
		
		// 加载语言文件
		Properties pro = new Properties();
        pro.put("init","true");
		messageData.put(lang, pro);
	}
	
	
	
	/**
	 * 获取语言Properties配置
	 * @param lang 语言标识
	 * @return
	 */
	public Properties get(String lang){
		if(messageData.containsKey(lang)){
			return messageData.get(lang);
		}else{
			lang = lang.split("-")[0];
			if(messageData.containsKey(lang)){
				return messageData.get(lang);
			}else{// 默认语言
				SystemConfig syscfg = SystemConfig.getInstance();
				return messageData.get(syscfg.getDefaultLanguage());
			}
		}
	}
	
	
	
	/**
	 * 设置
	 * @param lang 语言
	 * @param key 键
	 * @param value 值
	 */
	public void setProperty(String lang, String key, String value){
		Properties pro = this.messageData.get(lang);
		if(null == pro)
			throw new NullPointerException();
		pro.setProperty(key, value);
		defaultData.setProperty(key, "");// 设置默认数据
        updateKeys.add(key);
	}
	
	
	
	
	
	
	/**
	 * 持久化Properties文件
	 * @throws IOException
	 */
	public void storeProperty() throws IOException{


		DataBaseConfig dbcfg = DataBaseConfig.getInstance();
		String prefix = dbcfg.getPrefix();



		// 基础路径
		Set<String> sets = this.messageData.keySet();
		for(String lang : sets){
			Properties pro = messageData.get(lang);

            String sql = "update "+prefix+"sys_language set value=?, updateTime=sysdate() where lang=? and `key`=?";
            String sqlInsert = "insert into "+prefix+"sys_language values(null,?,?,?,sysdate())";

            String sqlExist = "select count(1) from "+prefix+"sys_language where lang=? and `key`=?";

            Iterator<Map.Entry<Object, Object>> it = pro.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> entry = it.next();
                String key = (String)entry.getKey();
                if(!checkIsUpdate(key)){
                    continue;
                }
                Object value = entry.getValue();
                boolean status = jdbcTemplate.queryForObject(sqlExist, Boolean.class, lang, key);
                if(status){
                    jdbcTemplate.update(sql, value,lang,key);
                }else{
                    jdbcTemplate.update(sqlInsert,lang,key,value);
                }

            }
		}
		// 更新默认数据
        String lang = "default";
        Properties pro = defaultData;
        String sql = "update "+prefix+"sys_language set value=?, updateTime=sysdate() where lang=? and `key`=?";
        String sqlInsert = "insert into "+prefix+"sys_language values(null,?,?,?,sysdate())";

        String sqlExist = "select count(1) from "+prefix+"sys_language where lang=? and `key`=?";

        Iterator<Map.Entry<Object, Object>> it = pro.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            boolean status = jdbcTemplate.queryForObject(sqlExist, Boolean.class, lang, key);
            if(status){
                jdbcTemplate.update(sql, value,lang,key);
            }else{
                jdbcTemplate.update(sqlInsert,lang,key,value);
            }
        }


        // 清空更新

        updateKeys.clear();

	}

    /**
     * 检查是否需要持久化
     * @param key
     * @return
     */
    private boolean checkIsUpdate(String key) {
	    for(String updateKey : updateKeys){
	        if(updateKey.equals(key)){
	            return true;
            }
        }
        return false;
    }


    public List<Locale> getLanguages() {
		return locales;
	}
	
	
	
	/**
	 * 获取下拉列表节点
	 * 查看系统配置会渲染到编辑界面.
	 *
	 * @return
	 */
	public String getSelectElement(){
		StringBuilder sb = new StringBuilder("<select id='selectLanguage' class='select_for_add' name='config.defaultlang'>");  
		Locale[] locales = Locale.getAvailableLocales(); 
		for(Locale l : locales){
			sb.append("<option value='").append(getLang(l)).append("'>")
				.append("[").append(getLang(l)).append("] &nbsp; ").append(l.getDisplayName())
			.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	
	
	
	/**
	 * 获取已经添加的下拉列表节点
	 * @return
	 */
	public String getReadySelectElement(){
		StringBuilder sb = new StringBuilder("<select id='selectLanguage' class='select_for_add' name='config.defaultlang'>");  
		for(Locale l : locales){
			sb.append("<option value='").append(getLang(l)).append("'>")
				.append("[").append(getLang(l)).append("] &nbsp; ").append(l.getDisplayName())
			.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	
	
	
	private String getLang(Locale l) {
		return l.getLanguage() + (l.getCountry().equals("")? "":"-"+l.getCountry());
		
	}



	/**
	 * 删除语言
	 * @param lang
	 * @throws IOException 
	 */
	public void remove(String lang) throws IOException {
		// 基础路径
		String basePath = WebRealPathHolder.REAL_PATH + "data" + File.separator + "international" + File.separator;
		
		
		// 删除Properties对象
		String langfile = basePath + "message_" + lang + ".properties";
		this.messageData.remove(lang);
		FileTools.deleteFile(new File(langfile));
		
		
		
		// 删除当前语言，并持久化
		Iterator<Locale> it =locales.iterator();
		while(it.hasNext()){
			String a = getLang(it.next());
			if(lang.equals(a)){ it.remove(); }
		}
	}
	
	
	
	public Set<Object> getDefaultProperties(){
		return this.defaultData.keySet();
	}


	
	/**
	 * 获取某个key的所有语言值
	 * @param key
	 * @return
	 */
	public Map<String,Object> getKeyMap(String key) {
		Iterator<Locale> it = locales.iterator();
		Map<String, Object> data = new HashMap<String, Object>();
		while(it.hasNext()){ 
			String lang = getLang(it.next());
			Properties pro = this.get(lang);
			data.put(lang, pro.getProperty(key));
		}
		return data;
	}



	
	/**
	 * 删除国际化中的键
	 * @param key
	 */
	public void removeKey(String key) {
		this.defaultData.remove(key);// 删除模板中的key
		
		// 删除各个国家的key
		Set<String> sets = this.messageData.keySet();
		for(String lang : sets){
			Properties pro = this.messageData.get(lang);
			pro.remove(key);
		}
	}


    public void loadFile(String lang, String file) throws IOException {
        Properties properties = messageData.get(lang);
        if(properties == null){
            properties = new Properties();
            messageData.put(lang, properties);
        }
        FileTools.load(file, properties);
    }


	/**
	 * 是否初始化
	 * @return boolean
	 */
	public boolean isInit() {
		return init;
	}
}
