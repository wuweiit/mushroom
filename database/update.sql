ALTER TABLE `mr_user`
ADD COLUMN `token`  varchar(32) NULL AFTER `ip`;

ALTER TABLE `mr_user`
  ADD COLUMN `email`  varchar(100) NULL AFTER `gid`;
-- 已经添加


===

ALTER TABLE `mr_user`
  ADD COLUMN `underwrite`  varchar(255) NULL AFTER `token`,
  ADD COLUMN `sex`  smallint(1) NULL AFTER `underwrite`;

ALTER TABLE `mr_user`
  ADD COLUMN `points`  bigint(22) NULL AFTER `sex`;



ALTER TABLE `mr_user`
  MODIFY COLUMN `token`  varchar(40) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL AFTER `ip`;

ALTER TABLE `mr_feedback`
  ADD COLUMN `userId`  int NULL AFTER `time`;



-- 20170219 添加分类
ALTER TABLE `mr_taolu`
  ADD COLUMN `category`  varchar(50) NULL AFTER `cid`;



-- 20170519
ALTER TABLE `db_mrcms`.`mr_user_menu` ADD COLUMN `end` int AFTER `type`;
ALTER TABLE `db_mrcms`.`mr_channel` ADD COLUMN `end` int AFTER `langkey`;

ALTER TABLE `db_mrcms`.`mr_channel` ADD COLUMN `categoryIds` varchar(255) AFTER `end`;

