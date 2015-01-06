package urkrewrite;

import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;

public class Test {


	private static final URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();
	
	public static void main(String[] args) { 
		urlRewrite.putRule("channel", "/{channel}.html");
		urlRewrite.putRule("content", "/{type}/thread-{id}.html");
		urlRewrite.putRule("page", "/{channel}-{page}.html");
				
		String url1 = "/cms?type=article&id=1&time=20142211"; 
		  
		System.out.println("E:" + urlRewrite.encoder(url1));
		System.out.println("D:" + urlRewrite.decoder(url1)); 
	}
}
