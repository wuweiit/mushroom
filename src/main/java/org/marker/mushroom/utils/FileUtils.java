package org.marker.mushroom.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtils {

	
	
	/**
	 * 顺序读取模板引擎的标签配置信息
	 * @param filePath 配置路径
	 * @param read
	 * @param character
	 * @throws Exception
	 */
	public static void read(String cfgFilePath, ReadLine read, String character) throws Exception{
		FileInputStream   __fis = new FileInputStream(cfgFilePath);//文件字节流
		InputStreamReader __isr = new InputStreamReader(__fis, character);//字节流和字符流的桥梁，可以指定指定字符格式
		BufferedReader    __br  = new BufferedReader(__isr);
		String temp = null;
		while ((temp = __br.readLine()) != null) {
			if(!temp.startsWith("#"))//如果是#号开头，代表注释
				read.rendLine(temp);
		}
		__br.close();__isr.close(); __fis.close();
	}
}
