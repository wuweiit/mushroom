package org.marker.mushroom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件工具类
 * @author marker
 * @date 2012-12-03
 * */
public class FileTools {
	
	/** 文本文件UTF-8编码 */
	public static final String FILE_CHARACTER_UTF8 = "UTF-8";
	/** 文本文件GBK编码 */
	public static final String FILE_CHARACTER_GBK  = "GBK";
 

	
	/**
	 * 获取文本文件内容
	 * @param filePath 文件路径
	 * @param character 字符编码
	 * @return String 文件文本内容
	 * @throws IOException 
	 * */
	public static final String getFileContet(File filePath,String character) throws IOException{
		return FileTools.getContent(filePath, character);
	}
	
	
	/**
	 * 写入文本文件内容
	 * @param filePath 文件路径
	 * @param character 字符编码 
	 * @throws IOException 
	 * */
	public static final void setFileContet(File filePath, String content, String character) throws IOException{
		FileTools.setContent(filePath, content, character);
	}	
	
	
	
	
	
	// 获取输入流中的内容
	public static final String getStreamContent(InputStream __fis, String character ) throws IOException{ 
		StringBuffer sb = new StringBuffer();  
		InputStreamReader __isr = new InputStreamReader(__fis, character);//字节流和字符流的桥梁，可以指定指定字符格式
		BufferedReader    __br  = new BufferedReader(__isr);
		
		String temp = null;
		while ((temp = __br.readLine()) != null) { sb.append(temp+"\n"); }
		__br.close();__isr.close(); __fis.close(); 
		return sb.toString();//返回文件内容
		
	}
	
	
	//内部处理文件方法
	private static String getContent(File filePath, String character) throws IOException{ 
		FileInputStream __fis = new FileInputStream(filePath);//文件字节流 
		return getStreamContent(__fis, character);//返回文件内容
	}
	
	//内部处理文件保存
	private static void setContent(File filePath, String content, String character) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter __pw = new PrintWriter(filePath, character);
		__pw.write(content); __pw.close();
	}
	
	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean deleteFolder(File delFolder) { 
		// 判断目录或文件是否存在
		if (!delFolder.exists()) { // 不存在返回 false
			return false;
		} else {
			// 判断是否为文件
			if (delFolder.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(delFolder);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(delFolder);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(File delFile) {
		// 路径为文件且不为空则进行删除
		if (delFile.isFile() && delFile.exists()) {
			return delFile.delete();
		}
		return false;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(File dirFile) {
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i]);
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i]);
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * 获取文件名称后缀
	 * @param path
	 * @return 后缀
	 */
	public static String getSuffix(String path){
        if (path.lastIndexOf(".") != -1 && path.lastIndexOf(".") != 0) {
            return path.substring(path.lastIndexOf(".") + 1);
        } else {
            return "";
        }
	}
	
	
	
	/**
	 * 加载配置文件 to Properties对象
	 * 采用编码：UTF-8
	 * @param pro
	 * @param path
	 * @throws IOException 
	 */
	public static void load(String path, Properties pro) throws IOException{
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, FILE_CHARACTER_UTF8);
		pro.load(isr); 
		isr.close(); fis.close();
	}
	
	/**
	 * 持久化Properties文件
	 * @param profile
	 * @param pro
	 * @throws IOException
	 */
	public static void store(String profile, Properties pro) throws IOException {
		FileOutputStream fos = new FileOutputStream(profile);
		OutputStreamWriter osw = new OutputStreamWriter(fos, FILE_CHARACTER_UTF8);
		pro.store(osw, "Power By marker 2014.");
		osw.close(); fos.close();
	}
	
	
	/**
	 * 读取持久化文件
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(String url) throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream(url); 
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		ois.close(); fis.close();
		return obj; 
	}
	
	/**
	 * 写入对象到持久化文件
	 * @param path
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObject(String path, Object obj) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.flush(); oos.close();fos.close();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 解压zip文件
	 * @param source zip文件
	 * @param target 解压目标文件夹
	 * @throws Exception
	 */
	public static void extract(String source, String target) throws Exception{
		File sourceFile = new File(source);
		if(!sourceFile.exists()){
			throw new Exception(source+" zip file not found!");
		} 
		ZipFile zipFile = new ZipFile(sourceFile); 
		Enumeration<?> entries = zipFile.entries();
		ZipEntry entry = null;
		while (entries.hasMoreElements()) {
			entry = (ZipEntry) entries.nextElement();
			String entryName = entry.getName();
			if (entryName.endsWith("/")) {// 
				continue;
			} else{// 解压
				writeFileByZipEntry(zipFile, entry, target + entryName);
			}
		}
	}
	
	
	/**
	 * 解压文件流操作操作
	 * @param ZipFile 
	 * @param ZipEntry entry
	 * @param path 解压路径
	 * */
	private static void writeFileByZipEntry(ZipFile zipFile,ZipEntry entry, String path)
			throws IOException {
		InputStream is = zipFile.getInputStream(entry);
		File file = new File(path);
		if (!file.getParentFile().exists())// 如果该文件夹不存在就创建
			file.getParentFile().mkdirs();
		OutputStream ow = new FileOutputStream(file, true);
		byte[] b = new byte[256];
		int len = is.read(b);
		while (len > 0) {
			ow.write(b, 0, len);
			len = is.read(b);
		}
		ow.flush();
		ow.close();
		is.close();
	}


	/**
	 * 复制文件
	 * 如果文件不存在，则创建
	 * 
	 * @param defaultfile 默认文件
	 * @param langfile 语言文件
	 * @throws IOException
	 */
	public static void copy(String defaultfile, String langfile) throws IOException {
		File file = new File(langfile);
		file.getParentFile().mkdirs();// 如果文件夹不存在则创建
		String content = getFileContet(new File(defaultfile), FileTools.FILE_CHARACTER_UTF8);
		setFileContet(file, content, FileTools.FILE_CHARACTER_UTF8);
	}





	

	
}
