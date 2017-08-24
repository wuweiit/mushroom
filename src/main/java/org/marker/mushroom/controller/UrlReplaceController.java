package org.marker.mushroom.controller;

import freemarker.template.utility.StringUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.marker.app.common.SessionAttr;
import org.marker.app.domain.MessageResult;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.ICommonDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author marker
 * */
@Controller
@RequestMapping("/admin/urlreplace")
public class UrlReplaceController extends SupportController  {


	@Autowired
	ICommonDao commonDao;


    /** 构造方法初始化一些成员变量 */
    public UrlReplaceController() {
        this.viewPath = "/admin/urlreplace/";
    }


    /**
     * URL替换页面
     *
     * @param request 请求对象
     * @return
     */
    @RequestMapping("/list")
    public Object list(
            HttpServletRequest request){
        ModelAndView view = new ModelAndView(this.viewPath+"list");

        String url = HttpUtils.getRequestURL(request);

        view.addObject("url", url);

        return view;
    }


    /**
     * 提交替换URL
     *
     * @param fromUrl 来自于URL
     * @param toUrl 替换为
     * @param request 请求对象
     * @return
     */
	@RequestMapping("/submit")
    @ResponseBody
	public Object submit(
            @RequestParam String fromUrl,
            @RequestParam String toUrl,
            HttpServletRequest request){

	    if (StringUtils.isEmpty(fromUrl)) {
            return new ResultMessage(false, "请填写完整数据");
        }
        if (StringUtils.isEmpty(toUrl)) {
	        return new ResultMessage(false, "请填写完整数据");
        }



        Object[] params =  new Object[]{fromUrl, toUrl};
        String prefix = getPrefix();

        String sql = "update "+prefix+"article set icon = REPLACE(icon,?,?)";
        commonDao.update(sql, params);
        sql = "update "+prefix+"article set content = REPLACE(content,?,?)";
        commonDao.update(sql, params);
        sql = "update "+prefix+"content set content = REPLACE(content,?,?)";
        commonDao.update(sql, params);

        sql = "update "+prefix+"channel set icon = REPLACE(icon,?,?)";
        commonDao.update(sql, params);

        sql = "update "+prefix+"link set icon = REPLACE(icon,?,?)";
        commonDao.update(sql, params);
        sql = "update "+prefix+"doctor set icon = REPLACE(icon,?,?)";
        commonDao.update(sql, params);


        return new ResultMessage(true,   "替换成功!");

    }
	
	
}
