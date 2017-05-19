package org.marker.mushroom.beans;

import java.io.File;
import java.util.Date;

import org.marker.mushroom.holder.WebRealPathHolder;

public class FileObject {
	private File file;
	
	
	public FileObject() { }
	
	public FileObject(File file) {
		this.file = file;
	}
	
	
	
	
	public String getName(){
		return file.getName();
	}
	
	public Date getLastModified(){
		if(file.isFile()){//文件夹
			return new Date(file.lastModified());
		}
		return null;
	}
	
	
	public String getsuffix(){
		if(file.isDirectory()){//文件夹
			return "";
		}else{
			int dot = getName().indexOf(".") + 1;
			if(dot != 0){
				return  getName().substring(dot, getName().length());
			}else{
				return "unknow";
			}
		}
	}
	
	
	public String getIcon(){
		String suffix = getsuffix();//文件格式
		if("".equals(suffix)){//文件夹
			return "images/file/fold.gif";
		}else if("html".equals(suffix) || "htm".equals(suffix)){//html
			return "images/file/htm.gif";
		}else if("txt".equals(suffix)){//txt
			return "images/file/txt.gif";
		}else if("png".equals(suffix)){//png
			return "images/file/pic.gif";
		}else if("jpg".equals(suffix)){//jpg
			return "images/file/jpg.gif";
		}else if("gif".equals(suffix)){//gif
			return "images/file/gif.gif";
		}else if("js".equals(suffix)){//js
			return "images/file/js.gif";
		}else if("css".equals(suffix)){//css
			return "images/file/css.gif";
		}else if("zip".equals(suffix)){//zip
			return "images/file/zip.gif";
		}else if("model".equals(suffix)){//module
			return "images/file/model.gif";
		}else if("plugin".equals(suffix)){// plugin
			return "images/file/plugin.gif";
		}else {
			return "images/file/unknow.gif";
		}
	}
	
	public long getLength(){
		if(file.isFile()){
			return file.length();
		}
		return 0;
	}

	public String getPath(){
		String path = file.getPath().replace(WebRealPathHolder.REAL_PATH,"/");;
		return path; //文件相对地址
	}
	
	
	public int getDirectory(){
		if(file.isDirectory()){
			return 1;
		}
		return 0;
	}
	
	
}
