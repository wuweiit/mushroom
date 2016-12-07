package org.marker.app.controller;

import org.marker.app.business.TaoluBusiness;
import org.marker.app.business.UserBusiness;
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

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 *  登录 接口
 * @author marker
 * */
@Controller
@RequestMapping("/api/login")
public class LoginController extends SupportController {


    @Autowired
    private UserBusiness userBusiness;



	public LoginController() {
			this.viewPath = "/app/login/";
	}


	/**
	 * Login
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody 
	public Object login(HttpServletRequest request,
			@RequestParam("user") String username,
			@RequestParam("pass") String password  ){


        return userBusiness.login(username, password);
	}


	
}
