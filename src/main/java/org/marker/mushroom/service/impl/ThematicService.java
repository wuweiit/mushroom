package org.marker.mushroom.service.impl;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ICategoryDao;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service(Services.THEMATIC)
public class ThematicService extends BaseService{


    @Autowired private ISupportDao commonDao;
    @Autowired private IChannelDao channelDao;

    @Autowired
	ICategoryDao categoryDao;


    public Page find(int currentPageNo, int pageSize, Map<String,Object> condition) {
		String keyword = (String) condition.get("keyword");
		String status = (String) condition.get("status");
		int cid = (Integer)condition.get("cid");
		int did = (Integer)condition.get("did");
        int userGroupId = (Integer)condition.get("userGroupId");
//		try {
//			keyword = new String(keyword.getBytes("ISO-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
        String prefix = config.getPrefix();


		String sql = "select a.id,a.title, a.author,a.views,a.status, a.time, concat('/cms?type=thematic','&id=',CAST(a.id as char),'&time=',DATE_FORMAT(a.time,'%Y%m%d'))  url, c.name as cname   from "+config.getPrefix()
				+"thematic as a "
				+ "left join "+prefix+"channel c on c.id = a.cid ";

        sql +=  "where a.status in ("+status+") ";
        if(userGroupId != 1){
            sql += " and a.cid in (select cid from "+prefix+"user_group_channel where gid = "+userGroupId+") ";
        }


		if(cid > 0) {
			// 计算子节点
			List<Channel> channelList = channelDao.findAll();

			List<Integer> cidList  = new ArrayList<>();
			cidList.add(cid);
			getTopCid(channelList , cidList, cid);

			String cids = StringUtils.join(cidList,",");

			sql += " and a.cid in ("+ cids +") ";
		}
		if(did >0){
			List<Category> categoryList = categoryDao.findAll();
			List<Integer> didList  = new ArrayList<>();
			didList.add(did);
			getTop2Cid(categoryList , didList, did);

			String dids = StringUtils.join(didList,",");

			sql += " and a.did in  ("+ dids +") ";
		}


		sql +=" and a.title like ? order by a.id desc";

		return commonDao.findByPage(currentPageNo, pageSize,  sql,'%'+keyword+'%');
	}

    /**
     * 计算子栏目
     * @param channelList
     * @param cidList
     * @param cid
     */
    private void getTopCid(List<Channel> channelList, List<Integer> cidList, int cid) {
        for(Channel channel: channelList){
            if(channel.getPid() == cid){
                cidList.add(channel.getId());
                getTopCid(channelList,cidList,channel.getId());
            }
        }
    }


	private void getTop2Cid(List<Category> list, List<Integer> cidList, int cid) {
		for(Category item: list){
			if(item.getPid() == cid){
				cidList.add(item.getId());
				getTop2Cid(list,cidList,item.getId());
			}
		}
	}


    public Map get(int id) {
		return commonDao.findById(Article.class, id);
	}




	/**
	 * 搜索文章
	 * @param param
	 * @return
	 */
	public Page search(WebParam param) {
        HttpServletRequest request = ActionContext.getReq();
        int currentPageNo = param.currentPageNo;
        int pageSize = 15;
        String keywords = param.keywords;

        DataBaseConfig config = DataBaseConfig.getInstance();
        String prefix = config.getPrefix();

        String sql = "select  M.*,C.name cname, concat('/cms?','type=article','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) url from "+prefix+"channel C "
                + "right join "+prefix+"article M on M.cid = C.id where M.title like ? ";

        return commonDao.findByPage(currentPageNo, pageSize,sql, '%'+keywords+'%');
	}
}
