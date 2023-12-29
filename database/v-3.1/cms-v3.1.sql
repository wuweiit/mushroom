
-- 2022-07-03 国际化表增加索引
ALTER TABLE `mr_sys_language`
    ADD INDEX `idx_key`(`key`);

-- 2022-07-30 留言增加邮箱地址
ALTER TABLE `mr_guestbook`
    ADD COLUMN `email` varchar(200) NULL COMMENT '邮箱地址' AFTER `id`;
ALTER TABLE `mr_guestbook`
    MODIFY COLUMN `status` int(1) UNSIGNED NULL DEFAULT NULL COMMENT '状态 0待审核 1已审核' AFTER `time`;


CREATE TABLE `mr_site`  (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
    `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点描述',
    `host` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `theme` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '站点' ROW_FORMAT = Dynamic;
