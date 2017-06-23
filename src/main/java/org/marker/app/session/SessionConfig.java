package org.marker.app.session;

/**
 *
 *
 * @author marker
 * Created by Administrator on 2016/12/21.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Session 配置
 *
 */
//@EnableRedisHttpSession
public class SessionConfig {


    /**
     * 默认构造
     */
    public SessionConfig(){
        System.out.println("---------------------------------------");
        System.out.println("- Spring Session Redis Is init.....");
        System.out.println("---------------------------------------");
    }

    // 自动注入指定的jedisConnectionFactory对象
    @Autowired
    @Qualifier("jedisConnectionFactory")
    private JedisConnectionFactory jedisConnectionFactory;



    @Bean
    public JedisConnectionFactory connectionFactory() {
        return jedisConnectionFactory;
    }


    /**
     * 防刷token
     * @return
     */
//    @Bean
//    public HttpSessionStrategy httpSessionStrategy() {
//        HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();
//        headerHttpSessionStrategy.setHeaderName("token");
//        return headerHttpSessionStrategy;
//    }


    @Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy() {
//        org.springframework.session.web.http
        CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
        cookieHttpSessionStrategy.setCookieName("S_ID");
        cookieHttpSessionStrategy.setSessionAliasParamName("token");
        return cookieHttpSessionStrategy;
    }
}