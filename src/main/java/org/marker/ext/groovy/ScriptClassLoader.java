package org.marker.ext.groovy;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.marker.mushroom.utils.FileTools;

public class ScriptClassLoader {

	public GroovyClassLoader loader ;
	
	private String src;
	
	public ScriptClassLoader(String src) {
		this.src = src;
		 ClassLoader parent = ScriptClassLoader.class.getClassLoader(); 
		 loader = new GroovyClassLoader(parent);
		 loader.addClasspath(src); 
	}
	
	
	/**
	 * Groovy脚本转换Class
	 * @param scriptName
	 * @return
	 * @throws CompilationFailedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Class<?> parseClass(String scriptName) throws CompilationFailedException, IOException, ClassNotFoundException{
		File filePath = new File(src + File.separator + scriptName + ".groovy");
		String text = FileTools.getFileContet(filePath, FileTools.FILE_CHARACTER_UTF8); 
		return loader.parseClass(text); 
	}
	
	
}
