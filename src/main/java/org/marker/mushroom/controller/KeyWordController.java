package org.marker.mushroom.controller;

import java.util.Collection;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.marker.mushroom.core.proxy.SingletonProxyKeyWordComputer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 关键字提取接口
 * 请求地址：http://ip:端口/项目/keyword.do
 * HTTP POST 参数：
 *  title 标题
 *  content 内容
 * @author marker
 * @version 1.0
 */
@Controller
public class KeyWordController {

	
	/**
	 * 关键字获取接口
	 * @return
	 */
	@RequestMapping(value="/keywords",method=RequestMethod.POST)
	@ResponseBody
	public String keyword(@RequestParam("title") String title, @RequestParam("content") String content){
		KeyWordComputer kwc = SingletonProxyKeyWordComputer.getInstance(); 
	  
		Collection<Keyword> result = kwc.computeArticleTfidf(title, content); 
//		System.out.println(result);
		String keyword  = result.toString();
		String keywords = keyword.substring(1, keyword.length()-1);// 截取两边的中括号
//		System.out.println(keywords);
		return keywords; 
	}
}
 
