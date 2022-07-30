
-- 2022-07-03 国际化表增加索引
ALTER TABLE `mr_sys_language`
    ADD INDEX `idx_key`(`key`);

-- 2022-07-30 留言增加邮箱地址
ALTER TABLE `mr_guestbook`
    ADD COLUMN `email` varchar(200) NULL COMMENT '邮箱地址' AFTER `id`;
ALTER TABLE `mr_guestbook`
    MODIFY COLUMN `status` int(1) UNSIGNED NULL DEFAULT NULL COMMENT '状态 0待审核 1已审核' AFTER `time`;