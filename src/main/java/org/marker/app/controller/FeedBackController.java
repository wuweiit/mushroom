package org.marker.app.controller;

import org.marker.app.business.UserBusiness;
import org.marker.app.dao.FeedBackDao;
import org.marker.app.domain.FeedBack;
import org.marker.app.domain.MessageResult;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.mushroom.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *  意见反馈 接口
 * @author marker
 * */
@Controller
@RequestMapping("/api/feedback")
public class FeedBackController extends SupportController {


    @Autowired
    private FeedBackDao feedBackDao;



	public FeedBackController() {
			this.viewPath = "/app/feedback/";
	}


	/**
	 * 反馈
	 */
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	@ResponseBody 
	public Object login(HttpServletRequest request,
			@RequestParam("content") String content,
			@RequestParam("userId") Integer userId,
			@RequestParam("nickname") String nickname ){
		FeedBack feedBack = new FeedBack();
		feedBack.setContent(content);
		feedBack.setTime(new Date());
		feedBack.setUserId(userId);
		feedBack.setNickname(nickname);
        String ip = HttpUtils.getRemoteHost(request);
        feedBack.setIp(ip);
		feedBackDao.save(feedBack);



        return MessageResult.success();
	}


	
}
