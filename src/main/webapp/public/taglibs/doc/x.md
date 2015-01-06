
### 标签简介

【遍历标签】

采用注释方式设计的遍历标签

如果觉得这种方式不能接受，可以自定义标签。

标签的开发完全使用正则匹配方式。




### 标签语法

`注意：使用[]包括的为注释，实际使用时，不包含[]。`
```
<!-- {[变量]:list table=([表名]) pid=([栏目ID]) order=(id asc)} -->



<!--{/list}-->
```

**标签属性：**

- table 表名称
- pid 栏目ID
- order 排序方式，格式：字段名 (asc|desc)



### 模板中应用标签

例子：
```
<!-- {c:list table=(article) pid=(5) order=(id asc)} -->
    <div class="page-header">
      <a href="${c.url}">
      	<h2 id="direction_jieshao">${c.time format=(yyyy-MM-dd)}: ${c.title!}</h2>
      </a>
    </div>
    <p>
      ${c.description!}
    </p>
<!--{/list}-->


```