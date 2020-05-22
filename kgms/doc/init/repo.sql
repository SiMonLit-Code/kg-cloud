
USE `kg_cloud_kgms`;

DROP TABLE IF EXISTS `repo_repository`;

CREATE TABLE `repo_repository` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '组件id',
  `name` varchar(50) NOT NULL COMMENT '组件名称',
  `type` varchar(50) NOT NULL DEFAULT 'DATA' COMMENT '组件类型',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `state` tinyint(1) DEFAULT '1' COMMENT '1启用状态 0停用状态',
  `rank` int(11) DEFAULT '1' COMMENT '排序',
  `remark` text COMMENT '描述',
  `config` text COMMENT '样式设置',
  `check_configs` text COMMENT '检测配置',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=903004 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `repo_repository_group`;

CREATE TABLE `repo_repository_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `group_name` varchar(50) NOT NULL COMMENT '分组名称',
  `desc` text COMMENT '描述',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=904 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `repo_repository_menu`;

CREATE TABLE `repo_repository_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) unsigned NOT NULL COMMENT '菜单id',
  `repository_id` int(11) unsigned NOT NULL COMMENT '组件id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `repo_repository_use_log`;

CREATE TABLE `repo_repository_use_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `repository_id` int(11) unsigned NOT NULL COMMENT '组件id',
  `menu_id` int(11) unsigned NOT NULL COMMENT '菜单id',
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

