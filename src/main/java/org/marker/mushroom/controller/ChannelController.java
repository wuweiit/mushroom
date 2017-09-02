package org.marker.mushroom.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.marker.mushroom.beans.*;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.dao.ContentDao;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.service.impl.ChannelService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.FileUtils;
import org.marker.mushroom.utils.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 

/**
 * 栏目管理
 *
 *
 *
 * @author marker
 *
 *
 * */
@Controller
@RequestMapping("/admin/channel")
public class ChannelController extends SupportController {


    /** 栏目Dao */
	@Autowired
    private IChannelDao channelDao;

	/** 栏目Service */
	@Autowired
    private ChannelService channelService;


	/** 内容Dao */
	@Autowired private ContentDao contentDao;
	/**
	 * 初始化视图路径
	 * */
	public ChannelController() {
	    this.viewPath = "/admin/channel/";
	}
	
	
	
	/** 添加栏目 */
	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request){

        List<String> templateList = TemplateUtils.getTempalteFiles();

        ModelAndView view = new ModelAndView(this.viewPath + "add");

        view.addObject("templateList", templateList);
        view.addObject("channels", channelService.getAllTree());
        return view;
	}
	
	//编辑栏目
	@RequestMapping("/edit")
	public ModelAndView edit( HttpServletRequest request){
		int id = Integer.parseInt(request.getParameter("id"));
		String sql = "select a.*,b.content from  "+getPrefix()+"channel a left join "+getPrefix()+"content b on a.contentId = b.id where a.id=?";


        Map<String,Object> bean =  dao.queryForMap(sql ,id);
        if(bean.get("contentId") == null){
            bean.put("contentId",0);
        }

        List<String> templateList = TemplateUtils.getTempalteFiles();
        ModelAndView view = new ModelAndView(this.viewPath + "edit");
        view.addObject("templateList", templateList);
        view.addObject("channel", bean);
        view.addObject("channels", channelService.getAllTree());
        return view;
	}

	//更新栏目
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Channel channel){

        Channel max = channelDao.findChildMaxSortMenuByPId(channel.getPid());
        if(max != null && (max.getSort() <= channel.getSort())){
            channel.setEnd(1);
        }

        // 更新同级end为0
        channelDao.updateEnd0(channel.getPid());

        int contentId = channel.getContentId();
        if(contentId == 0 ){
            Content content = new Content();
            content.setContent(channel.getContent());
            content.setModel("channel");
            contentDao.save(content);

            channel.setContentId(content.getId());
        }else{
            Content content = new Content();
            content.setId(channel.getContentId());
            content.setModel("channel");
            content.setContent(channel.getContent());
            contentDao.update(content);
        }


        if(channel.getId() == 0){
            if(commonDao.save(channel)){
                SystemConfig syscfg = SystemConfig.getInstance();
                try {
                    String path = WebRealPathHolder.REAL_PATH+"data"+File.separator+"template"+File.separator+"template.html";
                    String topath = WebRealPathHolder.REAL_PATH + "themes" + File.separator
                            + syscfg.getThemeActive() + File.separator + channel.getTemplate();

                    FileTools.copy(path, topath);

                } catch (IOException e) {
                    return new ResultMessage(true, "复制模板失败!");
                }



                return new ResultMessage(true, "提交成功!");
            }else{
                return new ResultMessage(false, "提交失败!");
            }


        }else{
            if(channelDao.update(channel)){
                return new ResultMessage(true, "更新成功!");
            }else{
                return new ResultMessage(false, "更新失败!");
            }
        }
	}
	
	

	
	
	
	/** 删除栏目 */
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(Channel.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
	
	
	
	/** 栏目列表 */
	@RequestMapping("/list")
	public ModelAndView list(){
        List<Map<String, Object>> list2 = channelService.getAllTree();

		ModelAndView view = new ModelAndView(this.viewPath + "list");

		view.addObject("channels", list2);
		return view;
	}



	@RequestMapping("/all")
	@ResponseBody
	public Object all(HttpServletRequest req){
		List<Channel> list = channelService.list();
		return list;
	}




}
