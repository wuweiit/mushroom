
### 文章模型简介

文章模型提供了系列内容发布系统常用的字段，标题、内容、发布时间、文章图标等等。

文章模型提供了访问量统计功能（PV）。

### 文章模型对象

对象名称： **article**

访问方式：
```
${article.title!}
// 感叹号作用：防止字段为null时异常。
```


### 文章模型字段

| 字段名称 | 功能    | 调用方式 |备注|
|--------|--------|--------|--------|
|   title |  标题  | ${article.title!} | - |
|   icon |  图标  | ${article.icon!} | - |



### HTML模板代码

```
<html>
<body>

<!-- {a:list table=(article) pid=(3) limit=(20) order=(id desc)} -->
	<div class="list-group-item">
    	<a href="#content_${a.id?c}">
        ${a.title length=(12)}
        </a>
    </div>
<!-- {/list} -->

</body>
</html>


```

### 结束语










