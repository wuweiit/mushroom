package org.marker.mushroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.SystemMessage;
import com.theokanning.openai.completion.chat.UserMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.config.impl.OpenAIConfig;
import org.marker.mushroom.system.domain.dto.AIModelPromptDTO;
import org.marker.mushroom.utils.HttpUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@RestController
@RequestMapping("/admin/openai")
public class OpenAIController {


    /** 配置对象 */
    @Resource
    private OpenAIConfig config;


    /**
     * SSE 流式响应· AI问答
     *
     * @param aiModelPromptDTO
     * @return
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamCompletion(
            @RequestBody AIModelPromptDTO aiModelPromptDTO, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String provide = aiModelPromptDTO.getProvide();
        String model = aiModelPromptDTO.getModel();
        String prompt = aiModelPromptDTO.getPrompt();
        String systemPrompt = aiModelPromptDTO.getSystemPrompt();

        if (StringUtils.isBlank(provide)) {
            provide = config.getType();
        }

        OpenAIConfig.OpenAIProperties openAIProperties = config.getByType(provide);
        if (StringUtils.isBlank(model)) {
            model = openAIProperties.getDefaultModel();
        }
        Duration timeoutDuration = Duration.ofMinutes(30L);

        SseEmitter emitter = new SseEmitter(timeoutDuration.toMillis());
        OpenAiService openAiService = new OpenAiService(openAIProperties.getApiToken(), timeoutDuration, openAIProperties.getApiUrl());

        List<ChatMessage> modelList = new java.util.ArrayList<>();
        if (StringUtils.isNotBlank(systemPrompt)) {
            modelList.add(new SystemMessage(systemPrompt, "system"));
        }
        modelList.add(new UserMessage(prompt, "user" ));

        // 创建流式请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model) //
                .messages(modelList)
                .stream(true) // 启用流式响应
                .build();

        // 异步处理流式响应
        new Thread(() -> {
            try {
                openAiService.streamChatCompletion(chatCompletionRequest)
                    .doOnError(emitter::completeWithError)
                    .timeout(30, TimeUnit.MINUTES)
                    .blockingForEach(chunk -> {
                        try {
                            // 处理每个 chunk 数据判断
                            if (chunk.getChoices()==null || chunk.getChoices().isEmpty() || chunk.getChoices().get(0).getMessage() == null) {
                                return;
                            }
                            // 发送每个 chunk 的内容
                            String chars = chunk.getChoices().get(0).getMessage().getContent();
//                                chars = chars.replaceAll("\n","[]");
                            if (StringUtils.isBlank(chars)) { // 为空则跳过
                                return;
                            }
                            System.out.print(chars);
                            JSONObject map = new JSONObject();
                            map.put("content", chars);
                            emitter.send(map.toJSONString() );

                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    });
                emitter.complete();// 完成响应
            } catch (Exception e) {
                log.error("SSE 异常", e);
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }


    /**
     * 新增一个获取模型清单接口
     *
     * @return 模型清单列表
     */
    @GetMapping(value = "/models")
    public List<OpenAIConfig.ModelItem> getModelList(HttpServletRequest request) {
        // 这里可以添加具体的业务逻辑，例如从数据库或其他服务获取模型清单
        List<OpenAIConfig.ModelItem> list = config.getModelList();
        String baseUrl = HttpUtils.getRequestURL(request);
        list.forEach(item -> {
            item.setIcon(baseUrl + item.getIcon());
        });
        return list;
    }

}

