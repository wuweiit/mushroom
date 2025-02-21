MRCMS
========

作者：marker

开源协议：MIT 
博客：www.yl-blog.com

MRCMS 是一款Java开发的内容管理系统，采用数据模型+模板+插件实现，内置提供了文章模型发布功能。

目标：快速构建中小网站！

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)

注意：拒绝996工作制公司请使用本项目。


### 构建项目

项目采用标准的Maven项目结构，可以导入Eclipse 、IDEA 开发工具，在构建时由于一些单元测试代码链接了数据库，因此构建需要跳过测试，具体操作如下：


```
mvn install -Dmaven.test.skip=true -P dev
```



### 立即运行
```
mvn jetty:run
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


### 文档

[http://docs.joggle.cn/](http://docs.joggle.cn/)


### 成功案例

排名不分先后。

- MRCMS官网 http://cms.yl-blog.com/
- 汉搜云 （该公司一些业务使用了MRCMS）
- 武汉妇联网 http://fl.hansap.cn/
- 武汉纺织行业协会 http://fzxh.hansap.com/
- 火凤凰官网 http://hanfis.com/
- 大众生态园林 http://www.dzstyl.com/
- 嘉禾粮油 http://www.whjiahe.com/
- 91农业网 http://www.91nongye.com/
- nuchain  http://www.nuchain.net/

注意：数据来源源HTTP 的 renfer 请求头，凡是通过网站调转到官网的都会被记录。
MRCMS并没有特意采集用户信息。



