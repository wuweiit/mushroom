package org.marker.mushroom.core.config.impl;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.holder.SpringContextHolder;

import java.util.Map;


/**
 * 微信配置
 *
 * @author marker
 */
@Setter
@Getter
@NoArgsConstructor
public final class WechatConfig extends ConfigDBEngine<WechatConfig> {


    /**
     * 存储类型 ALIYUN_OSS、LOCAL_OSS
     */
    private String token;

    /**
     * 阿里云oss 配置
     */
    private Map<String, WechatProperties> configs;

    /**
     * 获取实例
     *
     * @return SystemConfig
     */
    public static WechatConfig getInstance() {
        return SpringContextHolder.getApplicationContext().getBean(WechatConfig.class);
    }
    // 自定义 getStorageType 方法

    public WechatProperties getWechat(String key) {
        return configs.get(key);
    }

    /**
     * 阿里云OSS配置
     *
     * @author marker
     */
    @Data
    public static class WechatProperties {
        /**
         * 原始ID（开发者微信号）
         */
        private String originalId;
        /**
         * 服务器配置临牌
         */
        private String serverToken;

    }



}
