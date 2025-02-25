


function openaiChat(systemPrompt, userPrompt, callback) {
    let content = "";
    let model = window.aiModel.model;
    let provide = window.aiModel.provide;
    let url = "/admin/openai/stream.do?provide="+provide+"&model="+model+"&prompt="+userPrompt+"&systemPrompt="+systemPrompt;
    const eventSource = new EventSource(url);
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


    eventSource.onmessage = function(event) {
        // 将接收到的内容追加到页面上
        console.log(event)
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
    };

    eventSource.onerror = function(event) {
        console.log(event)
        eventSource.close();
    };
}