package org.marker.mushroom.ext.message;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.utils.FileTools;




/**
 * 消息数据上下文对象
 * 
 * @author marker
 * @version 1.0
 * @update 20170617 marker 开发了基于数据库的，没有必要使用文件存储的。
 */
@Deprecated
public class MessageContext {

	public static final String CONFIG_FILE = "config.obj";
	
	/** 默认数据 */
	private Properties defaultData = new Properties();
	
	private Map<String, Properties> data = new HashMap<String, Properties>();

	
	/** 语言列表 */
	private List<Locale> list = new ArrayList<Locale>(2);
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static MessageContext getInstance(){ 
		return SingletonHolder.instance;
	}

	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static MessageContext instance = new MessageContext();     
	}

	
	
	/**
	 * 准备就绪
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	public void reday() throws IOException, ClassNotFoundException, URISyntaxException{
		// 基础路径
		String basePath = WebRealPathHolder.REAL_PATH +File.separator+ "data" + File.separator + "international" + File.separator;
		
		
		// 1. 读取持久化国际语言对象文件
		String configFile =  basePath + "config.obj";
		this.list = (List<Locale>) FileTools.readObject(configFile);
		
		
		// 2. 加载国际化properties文件
		Iterator<Locale> it = this.list.iterator();
		while(it.hasNext()){
			String lang = getLang(it.next());
			Properties pro = new Properties();
			String profile = basePath + "message_" + lang + ".properties";
			FileTools.load(profile, pro);
			data.put(lang, pro);
		}
		
		
		// 3. 加载默认数据
		String defaultfile =  basePath + "message.properties";
		FileTools.load(defaultfile, defaultData);
	}
	
	
	
	/**
	 * 创建
	 * @param lang
	 * @throws Exception 
	 */
	public void create(String lang) throws Exception {
		if(this.data.containsKey(lang)){
			throw new Exception("this language is use!");
		}
		
		// 基础路径
		String basePath = WebRealPathHolder.REAL_PATH + "data" + File.separator + "international" + File.separator;
		
		
		// 添加语言到集合
		String[] strs = lang.split("-");
		if(strs.length > 1){
			list.add( new Locale(strs[0],strs[1]));
		}else{
			list.add(new Locale(strs[0]));
		}
		String url = basePath + "config.obj"; 
		FileTools.writeObject(url, list);// 持久化语言
		
		
		
		// 创建配置文件
		String defaultfile = basePath + "message.properties";
		String langfile    = basePath + "message_" + lang + ".properties";
		FileTools.copy(defaultfile, langfile);
	
		
		// 加载语言文件
		Properties pro = new Properties();
		FileTools.load(langfile, pro);
		data.put(lang, pro);
		
	}
	
	
	
	/**
	 * 获取语言Properties配置
	 * @param lang 语言标识
	 * @return
	 */
	public Properties get(String lang){
		if(data.containsKey(lang)){
			return data.get(lang);
		}else{
			lang = lang.split("-")[0];
			if(data.containsKey(lang)){
				return data.get(lang);
			}else{// 默认语言
				SystemConfig syscfg = SystemConfig.getInstance();
				return data.get(syscfg.getDefaultLanguage());
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
		Properties pro = this.data.get(lang);
		if(null == pro)
			throw new NullPointerException();
		pro.setProperty(key, value);
		defaultData.setProperty(key, "");// 设置默认数据
	}
	
	
	
	
	
	
	/**
	 * 持久化Properties文件
	 * @throws IOException
	 */
	public void storeProperty() throws IOException{
		// 基础路径
		String basePath = WebRealPathHolder.REAL_PATH + "data" + File.separator + "international" + File.separator;
		
		Set<String> sets = this.data.keySet();
		for(String lang : sets){
			Properties pro = data.get(lang);
			String profile = basePath + "message_" + lang + ".properties";
			FileTools.store(profile, pro);
		}
		// 更新默认数据
		String defaultfile = basePath + "message.properties";
		FileTools.store(defaultfile, defaultData);
	}



	public List<Locale> getLanguages() {
		return list;  
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
		for(Locale l : list){
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
		this.data.remove(lang);
		FileTools.deleteFile(new File(langfile));
		
		
		
		// 删除当前语言，并持久化
		Iterator<Locale> it =list.iterator();
		while(it.hasNext()){
			String a = getLang(it.next());
			if(lang.equals(a)){ it.remove(); }
		}
		String configFile = basePath + CONFIG_FILE; 
		FileTools.writeObject(configFile, list);
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
		Iterator<Locale> it = list.iterator();
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
		Set<String> sets = this.data.keySet();
		for(String lang : sets){
			Properties pro = this.data.get(lang);
			pro.remove(key);
		}
	}



	
}
