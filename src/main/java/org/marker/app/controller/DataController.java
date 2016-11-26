package org.marker.app.controller;

import org.markdown4j.Markdown4jProcessor;
import org.marker.app.business.TaoluBusiness;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.service.impl.ArticleService;
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
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  Faceinner 接口
 * @author marker
 * */
@Controller
@RequestMapping("/api/taolu")
public class DataController extends SupportController {

	// 文章Dao
	@Autowired IArticleDao articleDao;

	@Autowired ArticleService articleService;

	@Autowired CategoryService categoryService;

	@Autowired
	private TaoluBusiness taoluBusiness;


	public DataController() {
			this.viewPath = "/app/taolu/";

	}
	
	/**
	 * 文章列表接口(REST)
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody 
	public Object list(HttpServletRequest request, ModelMap model,
			@RequestParam("endId") int endId,
			@RequestParam("drection") int drection,// 1 和 -1
			@RequestParam("keyword") String keyword  ){
		String keywordVal = "";
		try {
			keywordVal = new String(keyword.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		return taoluBusiness.pullData(endId, drection, keywordVal);
	}


	/**
	 * 文章列表接口(REST)
	 * @return
	 */
	@RequestMapping(value = "/content", method = RequestMethod.GET)
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap model,
					   @RequestParam("id") int id ){
		return taoluBusiness.get(id);
	}
	
}
