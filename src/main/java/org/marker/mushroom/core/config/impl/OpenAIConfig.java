package org.marker.mushroom.core.config.impl;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.holder.SpringContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    public List<ModelItem> getModelList() {
        List<ModelItem> list = new ArrayList<>();
        OpenAIProperties ollama = getOllama();
        Arrays.stream(ollama.getModel().split(",")).forEach(model -> {
            ModelItem item = new ModelItem();
            item.setProvide("OLLAMA");
            item.setIcon("/admin/images/model-providers/ollama_small.svg");
            item.setModel(model);
            list.add(item);
        });
        OpenAIProperties deepSeek = getDeepSeek();
        Arrays.stream(deepSeek.getModel().split(",")).forEach(model -> {
            ModelItem item = new ModelItem();
            item.setProvide("DEEP_SEEK");
            item.setIcon("/admin/images/model-providers/deepseek_small.svg");
            item.setModel(model);
            list.add(item);
        });
        return list;
    }

    /**
     *  标准OpenAI 配置
     * @author marker
     */
    @Data
    public static class OpenAIProperties {

        private String icon;
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
    public static class ModelItem {
        private String provide;
        private String model;
        private String icon;
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
