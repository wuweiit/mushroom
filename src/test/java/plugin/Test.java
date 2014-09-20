package plugin;

public class Test {

	public static void main(String[] args) {
		
		// 
		String uri = "/plugin/guestbook/d/sa"; 
	 
		int gradient = uri.indexOf("/",7); 
		if(gradient != -1){  
			String pluginUrl = uri.substring(gradient+1,uri.length());
			
			int index = pluginUrl.indexOf("/");
			if(index != -1){
				String pluginName = pluginUrl.substring(0, index);
				String pluginCurl = pluginUrl.substring(index, pluginUrl.length());
				System.out.println(pluginName+" "+pluginCurl);
			}
		}
		
	}
}
