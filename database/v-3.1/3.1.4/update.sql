CREATE TABLE `mr_pancha` (
     `id` int(11) NOT NULL,
     `keywords` varchar(20) DEFAULT NULL,
     `article_id` int(11) DEFAULT NULL COMMENT '文章id',
     `type` varchar(20) DEFAULT NULL COMMENT 'baidu 百度网盘、360网盘',
     `pwd` varchar(10) DEFAULT NULL COMMENT '提取码',
     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     PRIMARY KEY (`id`),
     KEY `idx_articleId` (`article_id`),
     FULLTEXT KEY `idx_keywords` (`keywords`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;