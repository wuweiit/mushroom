package org.marker.mushroom.config;


import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;


/**
 * 微信公众号服务的配置
 * @author marker
 */
@Configuration
public class WechatService {


    @Resource
    private List<HandleMessageListener> handleMessageListeners;


    @Bean
    public DefaultSession defaultSession(){
        DefaultSession defaultSession = DefaultSession.newInstance();
        handleMessageListeners.forEach(defaultSession::addOnHandleMessageListener);
        return defaultSession;
    }
}
