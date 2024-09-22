INSERT INTO  `mr_sys_config`(`id`, `config`, `key`, `value`) VALUES (120, 'StorageConfig', 'storageType', NULL);
ALTER TABLE `mr_article`
    MODIFY COLUMN `stick` tinyint(1) NULL DEFAULT 0 COMMENT '是否置顶 1是 0否' AFTER `updateTime`;

ALTER TABLE `mr_article`
    MODIFY COLUMN `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片' AFTER `source`,
    MODIFY COLUMN `updateTime` datetime NULL DEFAULT NULL COMMENT '更新时间' AFTER `orginal`,
    ADD COLUMN `createTime` datetime NULL COMMENT '创建时间' AFTER `stick`;