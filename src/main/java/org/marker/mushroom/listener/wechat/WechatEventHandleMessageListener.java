package org.marker.mushroom.listener.wechat;

import org.marker.mushroom.utils.SpringUtils;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.msg.Msg4Event;
import org.marker.weixin.msg.Msg4Text;
import org.springframework.stereotype.Service;


/**
 * 微信事件
 * @author marker
 */
@Service
public class WechatEventHandleMessageListener extends HandleMessageAdapter {


    @Override
    public void onEventMsg(Msg4Event msg) {
        String eventType = msg.getEvent();
        if(Msg4Event.SUBSCRIBE.equals(eventType)){// 订阅
            System.out.println("关注人："+ msg.getFromUserName());
            System.out.println("参数值：" + msg.getEventKey());

            Msg4Text reMsg = new Msg4Text();
            reMsg.setFromUserName(msg.getToUserName());
            reMsg.setToUserName(msg.getFromUserName());
            reMsg.setCreateTime(msg.getCreateTime());

            reMsg.setContent("【菜单】\n" +
                    "1. 功能菜单\n" +
                    "2. 图文消息测试\n" +
                    "3. 图片消息测试\n");

            SpringUtils.getBean(DefaultSession.class)
             .callback(reMsg);//回传消息


        }else if(Msg4Event.UNSUBSCRIBE.equals(eventType)){// 取消订阅
            System.out.println("取消关注："+msg.getFromUserName());

        }else if(Msg4Event.CLICK.equals(eventType)){// 点击事件
            System.out.println("用户："+ msg.getFromUserName());
            System.out.println("点击Key："+msg.getEventKey());
        }
    }
}
