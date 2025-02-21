package org.marker.mushroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.UserMessage;
import com.theokanning.openai.service.OpenAiService;
import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.config.impl.OpenAIConfig;
import org.marker.mushroom.utils.HttpUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
     * @param provide
     * @param model
     * @param prompt
     * @return
     */
    @RequestMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamCompletion(
            @RequestParam(required = false) String provide, @RequestParam(required = false) String model,
            @RequestParam String prompt, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        if (StringUtils.isBlank(provide)) {
            provide = config.getType();
        }

        OpenAIConfig.OpenAIProperties openAIProperties = config.getByType(provide);
        if (StringUtils.isBlank(model)) {
            model = openAIProperties.getDefaultModel();
        }

        SseEmitter emitter = new SseEmitter();
        OpenAiService openAiService = new OpenAiService(openAIProperties.getApiToken(), openAIProperties.getApiUrl());

        // 创建流式请求
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model) //
                .messages(Collections.singletonList(new UserMessage(prompt, "user" )))
                .stream(true) // 启用流式响应
                .build();

        // 异步处理流式响应
        new Thread(() -> {
            try {
                openAiService.streamChatCompletion(request)
                        .doOnError(emitter::completeWithError)
                        .timeout(60, TimeUnit.SECONDS)
                        .blockingForEach(chunk -> {
                            try {
                                // 发送每个 chunk 的内容
                                String chars = chunk.getChoices().get(0).getMessage().getContent();
//                                chars = chars.replaceAll("\n","[]");
                                System.out.print(chars);
                                JSONObject map = new JSONObject();
                                map.put("content", chars);
                                emitter.send(map.toJSONString() );

                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        });

                // 完成响应
                emitter.complete();
            } catch (Exception e) {
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

