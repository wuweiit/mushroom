package org.marker.mushroom.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	
	
	/**
	 * 顺序读取模板引擎的标签配置信息
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


    /**
     * 获取文件集合
     *
     * @param path 路径
     * @param format 格式
     * @return
     */
	public static List<String> getPathList(String path, final String format) {
		final File file = new File(path);
        List<String> filePathList = new ArrayList<>();
        processPathList(filePathList, file , path , format);

		return filePathList;
	}


    /**
     * 递归算法
     * @param filePathList
     * @param file
     * @param path
     * @param format
     */
	private static void processPathList(List<String> filePathList, File file, String path, final String format) {
        if(file.isDirectory()){
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file1) {
                    if(file1.isDirectory()){
                        return true;
                    }
                    String suffix = FileTools.getSuffix(file1.getAbsolutePath());
                    if(suffix != null && suffix.equals(format)){
                        return true;
                    }
                    return false;
                }
            });

            for(File f : files){
                if (f.isDirectory()){
                    processPathList(filePathList, f , path , format);
                }else{
                    String pathItem = f.getPath();
                    filePathList.add(pathItem.replaceAll(path,""));
                }
            }
        }
    }



}
