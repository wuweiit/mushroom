package org.marker.mushroom.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.marker.mushroom.template.tags.res.WebDataSource;


/**
 * 模板模型(保存模板文件对应的包含模板，以及读取时间)
 * @author marker
 * */
public class TemplateModel {

	/** 修改时间 */
	private long readModified;
	
	/** 模板内容 */
	private String freemarkerTpl;
	
	/** 模板文件集合 */
	private List<File> files = new ArrayList<File>(5);
	
	/** 解析出的sql集合 */
	private List<WebDataSource> sqls = new ArrayList<WebDataSource>();
	
	public long getReadModified() {
		return readModified;
	}

	public void setReadModified(long readModified) {
		this.readModified = readModified;
	}

	public String getFreemarkerTpl() {
		return freemarkerTpl;
	}

	public void setFreemarkerTpl(String freemarkerTpl) {
		this.freemarkerTpl = freemarkerTpl;
	}

	public List<WebDataSource> getSqls() {
		return sqls;
	}

	public void setSqls(List<WebDataSource> sqls) {
		this.sqls = sqls;
	}
	
	
	/**
	 * 获取模板修改时间
	 * */
	public long lastModified(){
		long time = 0l;
		for(File file : files){
			time += file.lastModified();
		}
		return time;
	}
	
	
	public void putTemplateFile(File file){
		this.files.add(file);
	}
	
	
}
