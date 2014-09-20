package module;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.marker.ext.Msei;
import org.marker.ext.module.Module;
import org.marker.ext.module.ModuleContext;

public class Test {

	public static void main(String[] args) {
		 
		String rootPath = "D:\\Servers\\tomcat6\\webapps\\ROOT\\";// 项目根路径
	 
		String moduleDir = rootPath+"modules";// 模块目录
		
		// 缓存目录
		String cacheDir = rootPath+"modules"+File.separator+"cache";// 模块目录
		
		Map<String,String> configMap = new HashMap<String,String>();
	   	// 自动部署目录配置
        configMap.put("MSEI.auto.deploy.dir", moduleDir);
        // 缓存目录
        configMap.put("MSEI.cache.rootdir",cacheDir);
 
        // 日志级别
        configMap.put("MSEI.log.level", "1");
        // 自动安装并启动
        configMap.put("MSEI.auto.deploy.action", "install,start");

        Msei msei = null;
		try {
			msei = new Msei(configMap);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
  
        
        
        ModuleContext context = msei.getModuleContext();
		
        
        File file = new File(moduleDir);
        
        for(File f : file.listFiles()){
			String moduleName = f.getName();//  
			File moduleFile = new File(moduleDir + File.separator + moduleName);
			 
			try {
				Module module = context.install(moduleFile);
				
				module.start();// 启动模块 
				module.stop();
			} catch (Exception e) { 
				e.printStackTrace();
			}
        
        }
	}
}
