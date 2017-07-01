MRCMS
========

作者：marker

开源协议：GPL
https://choosealicense.com/licenses/gpl-2.0/

博客：www.yl-blog.com

MRCMS 是一款Java开发的内容管理系统，采用数据模型+模板+插件实现，内置提供了文章模型发布功能。

目标：快速构建中小网站！



### 构建项目

项目采用标准的Maven项目结构，可以导入Eclipse 、IDEA 开发工具，在构建时由于一些单元测试代码链接了数据库，因此构建需要跳过测试，具体操作如下：


```
mvn install -Dmaven.test.skip=true
```


###  登录系统

[http://localhost:8080/admin/login.do](http://localhost:8080/admin/login.do)
```
账号：admin
密码：1
```

操作界面

![image](http://static.oschina.net/uploads/space/2014/0721/123006_tPjQ_218887.png)

开源中国有详细说明分享地址：
[https://www.oschina.net/p/mrcms](https://www.oschina.net/p/mrcms)


