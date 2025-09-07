package pancha.src

import org.marker.mushroom.core.config.impl.DataBaseConfig
import org.marker.mushroom.dao.ICommonDao
import org.marker.mushroom.utils.SpringUtils
import org.marker.weixin.DefaultSession
import org.marker.weixin.HandleMessageAdapter
import org.marker.weixin.msg.Msg4Text
import org.springframework.stereotype.Service

/**
 * 自动恢复回调
 * @author marker
 */
@Service
public class PanchaAutoReplayHandleMessageListener extends HandleMessageAdapter {

    @Override
    public void onTextMsg(Msg4Text msg4Text) {
        String content = msg4Text.getContent();
        if(!content.startsWith("我要")){
            return;
        }
        String keywords = content.replace("我要","");

        ICommonDao commonDao = SpringUtils.getBean(ICommonDao.class)

        DataBaseConfig dbcfg = DataBaseConfig.getInstance();
        String prefix = dbcfg.getPrefix();
        String sql = """select p.id as pId,p.keywords pKeywords,p.type as pType, p.pwd as pPwd, p.url from ${prefix}pancha p 
         where p.keywords = ?
             order by p.id desc limit 1
            """;
        Object[] params = [keywords] as Object[]
        Map<String, Object> pancha = commonDao.queryForMap(sql, params);
        String replayContent = "盘查不存在！"
        if(pancha != null){
            String url = pancha.get("url") as String;
            replayContent = "网盘地址：" + url
        }
        // TODO msg4Text.getToUserName() 实现多账号自动恢复

        // 自动回复
        Msg4Text reMsg = new Msg4Text();
        reMsg.setToUserName(msg4Text.getFromUserName());
        reMsg.setFromUserName(msg4Text.getToUserName());
        reMsg.setCreateTime(msg4Text.getCreateTime());
        reMsg.setContent(replayContent);
        SpringUtils.getBean(DefaultSession.class)
                .callback(reMsg);//回传消息

    }

}
