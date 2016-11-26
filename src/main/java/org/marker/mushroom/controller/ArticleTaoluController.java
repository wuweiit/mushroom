package org.marker.mushroom.controller;

import org.markdown4j.Markdown4jProcessor;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.ArticleTaolu;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.dao.IArticleTaoluDao;
import org.marker.mushroom.service.impl.ArticleService;
import org.marker.mushroom.service.impl.ArticleTaoluService;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 文章管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/taolu")
public class ArticleTaoluController extends SupportController {

	// 文章Dao
	@Autowired
	IArticleTaoluDao articleDao;

	@Autowired
	ArticleTaoluService articleTaoluService;

	@Autowired CategoryService categoryService;

	public ArticleTaoluController() {
		this.viewPath = "/admin/taolu/";
		
	}
	
	//发布文章
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView(this.viewPath+"add"); 
		view.addObject("categorys", categoryService.list("article"));
		return view;
	}
	
	//编辑文章
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("article", commonDao.findById(ArticleTaolu.class, id));
		view.addObject("categorys", categoryService.list("article"));
		return view;
	}
	
	
	/**
	 * 持久化文章操作
	 * @param article
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(ArticleTaolu article, @RequestParam("cid") int cid){
		article.setTime(new Date());
		article.setCid(cid);// 这里是因为不能注入bean里
		
		String msg = "";
		if(article.getStatus() == 1){
			msg = "发布";
		}else{
			msg = "保存草稿";
		}
		if(article.getType() == 1){// marker
			try {
				String orginalText = article.getContent();
				article.setOrginal(orginalText);
				
				String html = new Markdown4jProcessor().process(orginalText);
				article.setContent(html);
				
			} catch (IOException e) { 
				e.printStackTrace();
			} 
		}
		
		if(commonDao.save(article)){ 
			return new ResultMessage(true,  msg + "成功!"); 
		}else{
			return new ResultMessage(false, msg + "失败!"); 
		}
	}
	
	
	//保存
	@ResponseBody
	@RequestMapping("/update")
	public Object update(@ModelAttribute("article") ArticleTaolu article){
		article.setTime(new Date());
		
		
		if(article.getType() == 1){// marker
			try {
				String orginalText = article.getContent();
				article.setOrginal(orginalText);
				
				String html = new Markdown4jProcessor().process(orginalText);
				article.setContent(html);
				
			} catch (IOException e) { 
				e.printStackTrace();
			} 
		}
		
		
		
		if(articleDao.update(article)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}
	
	
	
	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(ArticleTaolu.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
 
	
	//发布文章
	@RequestMapping("/list")
	public ModelAndView listview(){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		view.addObject("categorys", categoryService.list("article"));
		return view;
	}
	
	
	/**
	 * 文章列表接口(REST)
	 * @param currentPageNo
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody 
	public Object list(HttpServletRequest request, ModelMap model,
			@RequestParam("currentPageNo") int currentPageNo,
			@RequestParam("cid") int cid,
			@RequestParam("status") String status,
			@RequestParam("keyword") String keyword,
			@RequestParam("pageSize") int pageSize
		 
			){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("cid", cid);
		params.put("status", status);
		params.put("keyword", keyword);
		Page page = articleTaoluService.find(currentPageNo, pageSize, params);
		
		URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();
		
		String url = HttpUtils.getRequestURL(request);
		// 遍历URL重写
		Iterator<Map<String, Object>> it = page.getData().iterator();
		while(it.hasNext()){
			Map<String,Object> data = it.next();
			data.put("url", url + urlRewrite.encoder(data.get("url").toString())); 
		}
		return page;
	}
	
}
