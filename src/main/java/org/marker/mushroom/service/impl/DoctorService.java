package org.marker.mushroom.service.impl;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 医生服务
 *
 * @author marker
 */
@Service(Services.DOCTOR)
public class DoctorService extends BaseService{


    @Autowired private ISupportDao commonDao;
    @Autowired private IChannelDao channelDao;



    public Page find(int currentPageNo, int pageSize, Map<String,Object> condition) {
		String keyword = (String) condition.get("keyword");
		String status = (String) condition.get("status");
		int cid = (Integer)condition.get("cid");
        int userGroupId = (Integer)condition.get("userGroupId");


        String prefix = config.getPrefix();

		Page page;

        String sql = "select a.*, concat('/cms?type=doctor','&id=',CAST(a.id as char),'&time=',DATE_FORMAT(a.time,'%Y%m%d'))  url, c.name as cname ,'article' model from "
                +config.getPrefix()+"doctor as a "
                + "left join "+config.getPrefix()+"category c on c.id = a.did ";

        sql += " where a.status in ("+status+") and a.name like ? ";

        if(userGroupId != 1){
            sql += " and a.did in (select cid from "+prefix+"user_group_category where gid = "+userGroupId+") ";
        }

		if(cid != 0){
			// 计算子节点
            List<Channel> channelList = channelDao.findAll();

            List<Integer> cidList  = new ArrayList<>();
            cidList.add(cid);

            getTopCid(channelList , cidList, cid);

            String cids = StringUtils.join(cidList,",");

            sql+=" and a.cid in ("+ cids +") ";
		}
		sql += " order by a.id desc";

        page = commonDao.findByPage(currentPageNo, pageSize,  sql,'%'+keyword+'%');
		return page;
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


    public Map get(int id) {
		return commonDao.findById(Article.class, id);
	}
}
