
-- 3.1.2 版本

ALTER TABLE `mr_sys_config`
    MODIFY COLUMN `value` varchar(1000) NULL DEFAULT NULL AFTER `key`;