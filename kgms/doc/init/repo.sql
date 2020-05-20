CREATE TABLE `repo_repository` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '组件id',
  `name` varchar(50) NOT NULL COMMENT '组件名称',
  `type` varchar(50) NOT NULL COMMENT '组件类型',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1启用状态 0停用状态',
  `rank` int(11) NOT NULL COMMENT '排序',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `remark` text NOT NULL COMMENT '描述',
  `config` text COMMENT '样式设置',
  `check_configs` text COMMENT '检测配置',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `repo_repository_group` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group_id` INT(11) NOT NULL COMMENT '分组id',
  `group_name` VARCHAR(50) NOT NULL COMMENT '分组名称',
  `desc` TEXT COMMENT '描述',
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `repo_repository_use_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `repository_id` int(11) unsigned NOT NULL COMMENT '组件id',
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

USE `kg_cloud_kgms`;

insert  into `repo_repository_group`(`id`,`group_id`,`group_name`,`desc`,`create_time`,`update_time`) values
(1,0,'图谱扩展包','图谱扩展包提供扩展能力,包括存储能力扩展，构建能力扩展，计算引擎扩展，数据及模式扩展等','2020-05-19 17:17:33','2020-05-19 17:17:33'),
(2,1,'图谱数据扩展包','提供各类高性能存储版本','2020-05-19 17:18:16','2020-05-19 17:18:16'),
(3,0,'数仓扩展包','数仓扩展包提供扩展能力，包括存储能力扩展','2020-05-19 17:19:27','2020-05-19 17:19:27');