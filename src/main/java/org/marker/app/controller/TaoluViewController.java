package org.marker.app.controller;

import org.marker.app.business.TaoluBusiness;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.service.impl.ArticleService;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 *  Faceinner 接口
 * @author marker
 * */
@Controller
@RequestMapping("/app/taolu")
public class TaoluViewController extends SupportController {

	// 文章Dao
	@Autowired IArticleDao articleDao;

	@Autowired ArticleService articleService;

	@Autowired CategoryService categoryService;

	@Autowired
	private TaoluBusiness taoluBusiness;


	public TaoluViewController() {
			this.viewPath = "/app/taolu/";

	}
	

	/**
	 * 文章列表接口(REST)
	 * @return
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public Object list(HttpServletRequest request, ModelMap model,
					   @RequestParam("id") int id ){
		ModelAndView view = new ModelAndView(this.viewPath+"content");
		view.addObject("c", taoluBusiness.get(id));
		return view;
	}
	
}
