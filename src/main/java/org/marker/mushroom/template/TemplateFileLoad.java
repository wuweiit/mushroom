package org.marker.mushroom.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import love.cq.util.IOUtil;
import org.marker.mushroom.template.tags.res.WebDataSource;
import redis.clients.util.IOUtils;


/**
 * 模板文件内容加载器
 * 这里面维护了多个文件，以及对应模板中解析的SQL语句
 * @author marker
 * @version 1.0
 */
public class TemplateFileLoad {
	
	//包含模板匹配模式
	private static final Pattern includeTagPattern = Pattern.compile("<!--\\x20*#include\\x20+file=[\"\'](.+)[\'\"]\\x20*-->");
	
	
	// 若要做递归调用，只有一行一行的读取，这样
			
	// 加载的内容 	
	private StringBuilder content = new StringBuilder();
	 
	/** 文本文件UTF-8编码 */
	public static final String FILE_CHARACTER_UTF8 = "UTF-8";
	
	/** 修改时间 */
	private long readModified;
	

	/** 模板文件集合 */
	private List<File> files = new ArrayList<File>(5);
	
	/** 解析出的sql集合 */
	private List<WebDataSource> sqls = new ArrayList<WebDataSource>();
	
	
	
	/**
	 * 构造文件加载器
	 * @param file
	 * @throws IOException
	 */
	public TemplateFileLoad(File file) throws IOException {
		load(file);
		this.readModified = lastModified();// 读取时间
	}
	
	
	
	/**
	 * 逐行加载模板文件内容
	 * @param tplFile
	 * @throws IOException
	 */
	private void load(File tplFile) throws IOException{
		files.add(tplFile);// 加入模板文件维护集合


		FileInputStream   __fis = new FileInputStream(tplFile);//文件字节流
		InputStreamReader __isr = new InputStreamReader(__fis, FILE_CHARACTER_UTF8);//字节流和字符流的桥梁，可以指定指定字符格式
		BufferedReader    __br  = new BufferedReader(__isr);
		
		String temp = null;
		while ((temp = __br.readLine()) != null) {  
			File childFile = compre(tplFile, temp);
			if(childFile != null){
				load(childFile);// 递归调用加载引用模板
			}else{
				content.append(temp).append("\n");
			}
		}
		__br.close();__isr.close(); __fis.close();
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
	
	
	
	
	/**
	 * 查找匹配模板导入的代码，进行模板导入
	 * @param linedata
	 */
	private File compre(File currentFile, String linedata){
		Matcher matcher = includeTagPattern.matcher(linedata);
		while(matcher.find()){// 找到两个
    		String text = matcher.group(1);
    		StringBuilder filePath = new StringBuilder(currentFile.getParent());
    		filePath.append(File.separator).append(text);
    		File childFile = new File(filePath.toString());//模版文件
			return childFile; 
		}
		return null; 
	}



	/**
	 * 获取读取文件时间
	 * @return
	 */
	public long getReadModified() {
		return readModified;
	}



	/**
	 * 获取文件内容
	 * @return
	 */
	public String getContent() {
		return content.toString();
	}
    /**
     * 获取文件内容buffer
     * @return
     */
    public StringBuilder getContentBuffer() {
        return content;
    }



	public List<WebDataSource> getSqls() {
		return sqls;
	}



	public void setSqls(List<WebDataSource> sqls) {
		this.sqls = sqls;
	}
 

	
	
	
	
}
