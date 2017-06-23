package org.marker.app.controller;

import org.marker.app.business.TaoluBusiness;
import org.marker.app.business.UserBusiness;
import org.marker.app.common.ErrorCode;
import org.marker.app.common.SessionAttr;
import org.marker.app.domain.MessageResult;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.service.impl.ArticleService;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.GeneratePass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *  登录 接口
 * @author marker
 * */
@Controller
@RequestMapping("/api/user")
public class LoginController extends SupportController {


    @Autowired
    private UserBusiness userBusiness;



	public LoginController() {
			this.viewPath = "/app/user/";
	}


	/**
	 * Login
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody 
	public MessageResult login(HttpServletRequest request,
							   @RequestParam("user") String username,
							   @RequestParam("pass") String password,
							   HttpSession session) throws Exception {

		String sessionId = session.getId();

		// 验证用户是否存在(包含email和username)
		if(!userBusiness.existUserName(username)){
			return MessageResult.wrapErrorCode(ErrorCode.USER_NOT_EXISTS);
		}

		// 登录验证(根据用户名和密码查询)
		User user = userBusiness.findUser(username);
		if(user == null ){
			return MessageResult.wrapErrorCode(ErrorCode.USER_PASSWORD_ERROR);
		}
		String loginPasswordMd5 = GeneratePass.encode(password);
		if(!loginPasswordMd5.equals(user.getPass())){
			return MessageResult.wrapErrorCode(ErrorCode.USER_PASSWORD_ERROR);
		}

      	userBusiness.updateToken(user.getId(), sessionId);


		session.setAttribute(SessionAttr.USER_ID, user.getId());
		session.setAttribute(SessionAttr.USER_GROUP_ID, user.getGid());// 用户组





		Map<String,Object> result = new HashMap<>(6);
		result.put("id",user.getId());
		if(user.getSex() == 0){
			result.put("sexual","女");
		}else{
			result.put("sexual","男");
		}
		result.put("nickname",user.getNickname());
		result.put("points",user.getPoints());
		result.put("underwrite",user.getUnderwrite());
		result.put("token", sessionId);

		return MessageResult.success(result);

	}

    /**
     * register
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(HttpServletRequest request,
                        @RequestParam("user") String username,
                        @RequestParam("pass") String password,
                   		String code  ){

        // 验证验证码


        return userBusiness.register(username,password);

    }

	/**
	 * update
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public MessageResult update(HttpServletRequest request,
						   @RequestParam("field") String field,
						   @RequestParam("value") String value,
								HttpSession session){

		int userId = (Integer) session.getAttribute(SessionAttr.USER_ID);

		return userBusiness.updateField(userId, field,value);

	}
}
