INSERT INTO `user_info` (`uid`,`username`,`name`,`password`,`salt`,`state`) VALUES ('1', 'admin', '管理员', 'admin', 'salt', 0);
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (1,0,'用户管理',0,'0/','userInfo:view','menu','userInfo/userList');
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (2,0,'用户添加',1,'0/1','userInfo:add','button','userInfo/userAdd');
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (3,0,'用户删除',1,'0/1','userInfo:del','button','userInfo/userDel');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (1,0,'管理员','admin');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (2,0,'VIP会员','vip');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (3,1,'test','test');
INSERT INTO `sys_role_permission` VALUES ('1', '1');
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (1,1);
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (2,1);
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (3,2);
INSERT INTO `sys_user_role` (`role_id`,`uid`) VALUES (1,1);

CREATE TABLE `archives_2` (
  `virtual_id` int(16) NOT NULL AUTO_INCREMENT,
  `id` varchar(32) DEFAULT NULL,
  `file_id` varchar(32) DEFAULT NULL,
  `response_person` varchar(32) DEFAULT NULL,
  `title` varchar(32) DEFAULT NULL,
  `level` varchar(32) DEFAULT NULL,
  `date` varchar(32) DEFAULT NULL,
  `summary` varchar(128) DEFAULT NULL,
  `note` varchar(128) DEFAULT NULL COMMENT '备注',
  `page_count` int(16) DEFAULT NULL,
  `file_type` varchar(32) DEFAULT NULL COMMENT '文件类型',
  `secret_date` varchar(32) DEFAULT NULL COMMENT '保密年限(null代表无保密年限),有代表保密日期',
  `file_color` varchar(32) DEFAULT NULL COMMENT '文件颜色',
  PRIMARY KEY (`virtual_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `archives` (
  `virtual_id` int(16) NOT NULL AUTO_INCREMENT,
  `id` varchar(32) DEFAULT NULL,
  `file_id` varchar(32) DEFAULT NULL,
  `level` varchar(32) DEFAULT NULL,
  `date` varchar(32) DEFAULT NULL,
  `summary` varchar(128) DEFAULT NULL,
  `note` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`virtual_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
