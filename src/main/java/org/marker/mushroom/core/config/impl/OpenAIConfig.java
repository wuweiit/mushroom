package org.marker.mushroom.core.config.impl;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.holder.SpringContextHolder;


/**
 * OpenAI 配置
 *
 * @author marker
 */
@Setter
@Getter
@NoArgsConstructor
public final class OpenAIConfig extends ConfigDBEngine<OpenAIConfig> {


    /**
     * 类型 DEEP_SEEK、OPEN_AI、OLLAMA
     */
    private String type;

    /**
     * deepSeek 配置
     */
    private OpenAIProperties deepSeek;

    /**
     * ollama 配置
     */
    private OpenAIProperties ollama;

    /**
     * 获取实例
     *
     * @return SystemConfig
     */
    public static OpenAIConfig getInstance() {
        return SpringContextHolder.getApplicationContext().getBean(OpenAIConfig.class);
    }

    public OpenAIProperties getByType(String provide) {
        if ("DEEP_SEEK".equals(provide)) {
            return this.getDeepSeek();
        }
        if ("OLLAMA".equals(provide)) {
            return this.getOllama();
        }
        throw new RuntimeException("不支持的ai提供商");
    }

    /**
     *  标准OpenAI 配置
     * @author marker
     */
    @Data
    public static class OpenAIProperties {
        /**
         * apiUrl
         */
        private String apiUrl;
        /**
         * apiToken
         */
        private String apiToken;
        /**
         * 模型 逗号间隔
         */
        private String model;


        /**
         * 获取默认模型，默认获取第一个
         * @return
         */
        public String getDefaultModel() {
            if (StringUtils.isNotBlank(this.model)) {
                return this.model.split(",")[0];
            }
            throw new RuntimeException("不支持的模型");
        }
    }

    @Data
    public static class Model {
        /**
         * 模型名称
         */
        private String name;

    }

    public String getType() {
        return this.properties.getProperty("type");
    }

    public OpenAIProperties getOllama() {
        OpenAIProperties openAIProperties = new OpenAIProperties();
        openAIProperties.setApiUrl(this.get("ollama.apiUrl"));
        openAIProperties.setApiToken(this.get("ollama.apiToken"));
        openAIProperties.setModel(this.get("ollama.model"));
        return openAIProperties;
    }


    public OpenAIProperties getDeepSeek() {
        OpenAIProperties openAIProperties = new OpenAIProperties();
        openAIProperties.setApiUrl(this.get("deepSeek.apiUrl"));
        openAIProperties.setApiToken(this.get("deepSeek.apiToken"));
        openAIProperties.setModel(this.get("deepSeek.model"));
        return openAIProperties;
    }
}
