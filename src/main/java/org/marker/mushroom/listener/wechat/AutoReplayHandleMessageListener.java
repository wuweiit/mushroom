package org.marker.mushroom.listener.wechat;

import org.marker.mushroom.utils.SpringUtils;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.msg.*;
import org.springframework.stereotype.Service;


/**
 * 自动恢复回调
 * @author marker
 */
@Service
public class AutoReplayHandleMessageListener implements org.marker.weixin.HandleMessageListener {

    @Override
    public void onTextMsg(Msg4Text msg4Text) {
        // 自动回复
        Msg4Text reMsg = new Msg4Text();
        reMsg.setToUserName(msg4Text.getFromUserName());
        reMsg.setFromUserName(msg4Text.getToUserName());
        reMsg.setCreateTime(msg4Text.getCreateTime());
        reMsg.setContent("您说的是："+msg4Text.getContent());
        SpringUtils.getBean(DefaultSession.class)
                .callback(reMsg);//回传消息
    }

    @Override
    public void onImageMsg(Msg4Image msg4Image) {

    }

    @Override
    public void onEventMsg(Msg4Event msg4Event) {

    }

    @Override
    public void onLinkMsg(Msg4Link msg4Link) {

    }

    @Override
    public void onLocationMsg(Msg4Location msg4Location) {

    }

    @Override
    public void onVoiceMsg(Msg4Voice msg4Voice) {

    }

    @Override
    public void onErrorMsg(int i) {

    }

    @Override
    public void onVideoMsg(Msg4Video msg4Video) {

    }
}
