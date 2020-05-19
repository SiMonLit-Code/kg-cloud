CREATE TABLE `kg_cloud_kgms`.`repo_repository` (
  `id` INT (11) UNSIGNED NOT NULL COMMENT '组件id',
  `name` VARCHAR (50) NOT NULL COMMENT '组件名称',
  `group` INT (11) NOT NULL COMMENT '分组id',
  `state` TINYINT (1) NOT NULL DEFAULT 1 COMMENT '1启用状态 0停用状态',
  `rank` INT (11) NOT NULL COMMENT '排序',
  `menu_id` INT (11) NOT NULL COMMENT '菜单id',
  `remark` TEXT (256) NOT NULL COMMENT '描述',
  `config` TEXT (256) COMMENT '样式设置',
  `checkConfigs` TEXT (256) COMMENT '检测配置',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB CHARSET = utf8mb4;


CREATE TABLE `kg_cloud_kgms`.`repo_repository_use_log` (
  `id` INT (11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `repository_id` INT (11) UNSIGNED NOT NULL COMMENT '组件id',
  `menu_id` INT (11) UNSIGNED NOT NULL COMMENT '菜单id',
  `user_id` VARCHAR (20) NOT NULL COMMENT '用户id',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB CHARSET = utf8mb4;



