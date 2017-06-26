package urkrewrite;

import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.urlrewrite.URLRewriteEngine;

public class Test {

// /
	private static final URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();
	
	public static void main(String[] args) {
//		urlRewrite.putRule("channel", "/{channel}.html");
		urlRewrite.putRule("content", "/{type}/{id}.html");
        urlRewrite.putRule("thematicPage", "/{type}/{id}/{page}.html");
//		urlRewrite.putRule("page", "/{channel}-{page}.html");
				
		String url1 = "/cms?type=thematic&id=1&page=1";


		System.out.println("E:" + urlRewrite.encoder(url1));
//		System.out.println("D:" + urlRewrite.decoder(url1));


		String url2 = "/cms?type=article&id=364&time=20170622";


		System.out.println("E:" + urlRewrite.encoder(url2));
//		System.out.println("D:" + urlRewrite.decoder(url2));


	}
}
