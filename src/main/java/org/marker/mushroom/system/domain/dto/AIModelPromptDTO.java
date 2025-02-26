package org.marker.mushroom.system.domain.dto;

import lombok.Data;

@Data
public class AIModelPromptDTO {
    // 用户提示词
    private String prompt;
    // 系统提示词
    private String systemPrompt;
    // AI模型
    private String model;
    // 提供商
    private String provide;
}
