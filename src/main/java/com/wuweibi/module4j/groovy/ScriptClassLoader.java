package com.wuweibi.module4j.groovy;

import com.wuweibi.utils.FileTools;
import groovy.lang.GroovyClassLoader;
import javassist.ClassPool;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.File;
import java.io.IOException;


/**
 *
 * 脚本加载器
 *
 * @author marker
 */
public class ScriptClassLoader {

	/** Groovy加载器 */
	private GroovyClassLoader loader;

	/** 目标路径 */
	private String src;
	
	public ScriptClassLoader(String src) {
		this.src = src;
		 ClassLoader parent = ScriptClassLoader.class.getClassLoader();
		 loader = new GroovyClassLoader(parent);
		 loader.addClasspath(src);
	}

	public ScriptClassLoader(ClassPool cpool, String src) {
		this.src = src;
		ClassLoader parent = cpool.getClassLoader();
		loader = new GroovyClassLoader(parent);
		loader.addClasspath(src);

	}


	/**
	 * Groovy脚本转换Class
	 * @param scriptName 脚本名称
	 * @return  Class
	 * @throws CompilationFailedException 异常
	 * @throws IOException 异常
	 * @throws ClassNotFoundException 异常
	 */
	public Class<?> parseClass(String scriptName) throws CompilationFailedException, IOException, ClassNotFoundException {
		File filePath = new File(src + File.separator + scriptName + ".groovy");
		String text = FileTools.getFileContet(filePath, FileTools.FILE_CHARACTER_UTF8);
		return loader.parseClass(text);
	}
	
	
}
