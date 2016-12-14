ALTER TABLE `mr_user`
ADD COLUMN `token`  varchar(32) NULL AFTER `ip`;

ALTER TABLE `mr_user`
  ADD COLUMN `email`  varchar(100) NULL AFTER `gid`;

