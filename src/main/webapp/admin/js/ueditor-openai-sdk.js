


function openaiChat(systemPrompt, userPrompt, callback) {
    let content = "";
    let model = window.aiModel.model;
    let provide = window.aiModel.provide;
     let data = {
        provide: provide,
        model: model,
        systemPrompt: systemPrompt,
        prompt: userPrompt,
    }

    let dynamicDiv = document.createElement('p');
    dynamicDiv.id = 'dynamicDiv_'+Math.random()*1000000000;
    editor.execCommand('insertHtml', dynamicDiv.outerHTML);
    // 获取目标容器
    // 获取 UEditor 的 iframe 文档对象
    var editorDocument = editor.iframe.contentDocument || editor.iframe.contentWindow.document;
    // 根据 ID 找到插入的 DOM 元素
    var targetElement = editorDocument.getElementById(dynamicDiv.id);
    // if (targetElement) {
    //     // 更新元素内容
    //     targetElement.textContent = '更新后的内容';
    // }
    let randomGif = String(Math.floor(Math.random() * 40) + 1).padStart(2, '0');
    targetElement.innerHTML = "<img src='http://img.baidu.com/hi/tsj/t_00"+randomGif+".gif' />AI思考中...";  // 展示AI思考中

    SSE.fetchEventSource("/admin/openai/stream.do", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
        onmessage(event) {
            console.log('Received message:', event.data);
            // 这里可以根据接收到的流式数据更新前端界面
            // 将接收到的内容追加到页面上
            let obj = JSON.parse(event.data);

            content += obj.content;
            // 解析markdown->html
            const md = markdownit({
                html: true,
                linkify: true,
                typographer: true ,
                breaks:       true, // \n转 M<br>
            })
            // content = content.replaceAll('\n','<br/>')
            let doc = md.render(content);

            targetElement.innerHTML = doc;

            // editor.setContent(oldContent +"<br/>"+ doc );
            // 当需要时调用sync来更新视图
            editor.sync();
        },
        onclose() {
            console.log('Connection closed by server');
        },
        onerror(err) {
            console.error('Error received:', err);
            SSE.close();
        },
    });
}