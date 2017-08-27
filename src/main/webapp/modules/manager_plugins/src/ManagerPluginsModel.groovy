
import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.alias.SQL;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.springframework.util.StringUtils;


/**
 * 文章模型实现
 * @author marker
 * @version 1.0
 */
class ManagerPluginsModelImpl extends ContentModel{


     ManagerPluginsModelImpl() {
        Map<String,Object> config = new HashMap<String,Object>();
        config.put("icon", "images/demo.jpg");
        config.put("name", "插件模型");
        config.put("author", "marker");
        config.put("version", "0.1");
        config.put("type", "plugins");
        config.put("template", "contentplugin.html");
        config.put("description", "系统内置模型");
        config.put("prefix", "ext_");
        configure(config);
    }



    /**
     * 抓取内容
     */
    public void fetchContent(Serializable cid) {
        String sysPrefix = getPrefix();//表前缀，如："mr_"
        HttpServletRequest request = ActionContext.getReq();

        String sql = "select M.*,C.name cname, concat('/cms?','type=plugins','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) url from ${sysPrefix}channel C right join ext_plugins M on M.cid = C.id  where M.id=?";

        Object content = commonDao.queryForMap(sql, cid);
        commonDao.update("update ext_plugins set views = views+1 where id=?", cid);// 更新浏览量

        // 必须发送数据
        request.setAttribute("content", content);
    }



    /**
     * 处理分页
     */
    public Page doPage(WebParam param) {
        String prefix = getPrefix();//表前缀，如："mr_"



        long id = param.channel.getId();

        String categoryIds = ""+id;
//        IChannelDao channelDao = SpringContextHolder.getBean(IChannelDao.class);
//
//
//
//
//		String categoryIds = param.channel.getCategoryIds();
//		if(StringUtils.isEmpty(categoryIds)){
//			 = "0";
//		}

        StringBuilder sql = new StringBuilder();
        sql.append("select A.*,C.name as cname,concat('type=plugins&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ")
                .append("ext_plugins").append(SQL.QUERY_FOR_ALIAS)
                .append(" join ").append(prefix).append("channel").append(" C on A.cid=C.id ")
                .append(" where 1=1 and ").append("A.cid in ("+categoryIds+") order by A.time desc").append(param.extendSql!= null?param.extendSql:"");

        return commonDao.findByPage(param.currentPageNo, param.pageSize, sql.toString());
    }



    /**
     * 前台标签生成SQL遇到该模型则调用模型内算法
     * @param tableName 表名称
     * */
    public StringBuilder doWebFront(String tableName, WebDataSource webDataSource) {
        String cprefix = webDataSource.getPrefix(); // 获取表前缀

        StringBuilder sql = new StringBuilder("select  M.*,C.name cname, concat('type=plugins','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) url from" +
                " " + webDataSource.getSystemPrefix() + "channel C "
                + "right join " + (StringUtils.isEmpty(cprefix) ? "" : cprefix) + "plugins M on M.cid = C.id");
        return sql;
    }



    /**
     * 备份数据
     */
    public void backup(){


    }



    /**
     * 恢复数据
     */
    public void recover(){

    }

}
