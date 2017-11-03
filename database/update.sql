
-- 3.1.1 版本

ALTER TABLE `mr_project` ADD COLUMN `stick` int AFTER `extJson`;
ALTER TABLE `mr_article` ADD COLUMN `stick` int AFTER `updateTime`;



ALTER TABLE `mr_honer` CHANGE COLUMN `icon` `icon` text DEFAULT NULL;