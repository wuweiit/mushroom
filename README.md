mushroom
========

MR 代表的是mushroom，翻译中文为蘑菇。名称灵感来源于七采蘑菇这么一个说法，根据MRCMS自身的一些特点，
因此命名为蘑菇内容管理系统。后台采用Java语言和与Java无缝集成的Groovy脚本语言作为支撑，实现系统热部署扩展功能。


官方网站：http://cms.yl-blog.com



### 项目背景

在开放、自由的中国互联网中，CMS领域做的公司或者个人很多，其中使用PHP、ASP、JSP等语言开发的为主流语言。PHP、ASP、JSP优点在于他们的动态执行代码。这里我们就说说JSP，每次更新都要编译为class字节码，可能有时候还会出现问题，而且面向过程开发，维护性差。受到目前主流的MVC开发模式影响，那我们可以使用Servlet+JSP来实现啊，不错，我们是这样做的！但是后台的开发效率要提高，这里我们选择了SpringMVC作为后台的框架。我们要做的就是颠覆，什么呢？ ”No JSP！“，在项目的架构中，不使用JSP，我们同样达到了在线实时动态扩展功能，而不必重启服务器带来的时间成本。（更多项目历史性资料登录官方网站浏览）
       
       
       
       
### 技术架构 

服务端后台：SpringMVC、Spring Data、URL规则引擎、缓存、数据库连接池、线程池、Freemarker模板引擎、线程安全相关技术、性能优化

  Web前端：HTML5、CSS3、JQuery(及其插件)、Ueditor、Echarts、缓存、前端性能优化、浏览器兼容处理（不支持IE8）、字体图标

  设计风格：扁平化设计

  测试工具：Spring Test、Apache JMeter、各种浏览器(Chrome/Firefox/Safari/Opera/IE11)

（项目没有使用SVN、Git、Maven、Grunt、Gradle这样的工具，我们会用的，一步一步到位。）



### 系统架构图

待绘制...



### 运行环境

服务器(经过测试的)：


    window server(Jre6/tomcat6/mysql5)
    Linux(openjdk6/tomcat7/mysql5)

（如果有必要，同样可以搭建Nginx服务器做反向代理，MRCMS支持获取用户真实IP地址）




### 测试结果分享

测试环境：{系统: win8.1，内存: 8G，CPU: 2.6GHz，硬盘：7200转/秒}

通过Apache的Jmeter测试并发500次/秒，系统仍然稳定运行，平均访问消耗时长为0.3秒左右。

如果你有什么需求，可以关注官方微信公众号，发送给公众号，我将记录你的意见或者建议。
