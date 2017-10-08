package org.marker.mushroom.controller;

import io.github.gitbucket.markedj.Marked;
import org.marker.app.common.SessionAttr;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.Project;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.ProjectDao;
import org.marker.mushroom.service.impl.ArticleService;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.service.impl.ChannelService;
import org.marker.mushroom.service.impl.ProjectService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.urlrewrite.URLRewriteEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 文章管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/project")
public class ProjectController extends SupportController {

	// 文章Dao
	@Autowired
	ProjectDao projectDao;

	@Autowired
	ProjectService projectService;

	@Autowired CategoryService categoryService;

	@Autowired
	IChannelDao channelDao;

	public ProjectController() {
		this.viewPath = "/admin/project/";
		
	}
	@Autowired
	ChannelService channelService;

	
	//发布文章
	@RequestMapping("/add")
	public ModelAndView add(HttpSession session){
		ModelAndView view = new ModelAndView(this.viewPath+"add");
		int userGroupId = (int)session.getAttribute(SessionAttr.USER_GROUP_ID);
		view.addObject("channels", channelService.getUserGroupChannel(userGroupId));
		view.addObject("categorys", categoryService.getUserGroupCategory(userGroupId));

		return view;
	}
	
	//编辑文章
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id,HttpSession session){
		ModelAndView view = new ModelAndView(this.viewPath + "edit");
		view.addObject("article", commonDao.findById(Project.class, id));
		int userGroupId = (int)session.getAttribute(SessionAttr.USER_GROUP_ID);
		view.addObject("channels", channelService.getUserGroupChannel(userGroupId));
		view.addObject("categorys", categoryService.getUserGroupCategory(userGroupId));
		return view;
	}
	
	
	/**
	 * 持久化文章操作
	 * @param entity 实体
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(Project entity, @RequestParam("cid") int cid){
		entity.setTime(new Date());
		entity.setCid(cid);// 这里是因为不能注入bean里
		
		String msg = "";
		if(entity.getStatus() == 1){
			msg = "发布";
		}else{
			msg = "保存草稿";
		}
		if(commonDao.save(entity)){
			return new ResultMessage(true,  msg + "成功!"); 
		}else{
			return new ResultMessage(false, msg + "失败!"); 
		}
	}
	
	
	//保存
	@ResponseBody
	@RequestMapping("/update")
	public Object update(@ModelAttribute("article") Project entity){

		if(projectDao.update(entity)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}
	
	
	
	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(Project.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}


	// 审核文章
    @ResponseBody
    @RequestMapping("/audit")
	public Object audit(@RequestParam("id") Integer id){
        boolean status = projectDao.updateStatus(id, 1);
        if(status){
            return new ResultMessage(true,"审核成功!");
        }else{
            return new ResultMessage(false,"审核删除!");
        }
    }
	
	//发布文章
	@RequestMapping("/list")
	public ModelAndView listview(HttpSession session){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
        int userGroupId = (int)session.getAttribute(SessionAttr.USER_GROUP_ID);
        view.addObject("channels", channelService.getUserGroupChannel(userGroupId));
		view.addObject("categorys", categoryService.getUserGroupCategory(userGroupId));

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
					   @RequestParam("did") int did,
			@RequestParam("status") String status,
			@RequestParam("keyword") String keyword,
			@RequestParam("pageSize") int pageSize,
                       HttpSession session
		 
			){



		Map<String,Object> params = new HashMap<String,Object>(4);
		params.put("cid", cid);
		params.put("did", did);
		params.put("status", status);
        int userGroupId = (int)session.getAttribute(SessionAttr.USER_GROUP_ID);
        params.put("userGroupId", userGroupId);




		params.put("keyword", keyword);
		Page page = projectService.find(currentPageNo, pageSize, params);
		
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
