SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '应用名称',
  `is_enable` tinyint(1) NULL DEFAULT 0 COMMENT '允许状态',
  `is_checked` tinyint(1) NULL DEFAULT 0 COMMENT '选中状态',
  `menu_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
  `rank` int(11) NULL DEFAULT NULL COMMENT '排序',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '配置',
  `create_at` datetime(3) NULL DEFAULT NULL,
  `update_at` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_param
-- ----------------------------

DROP TABLE IF EXISTS `system_param`;
CREATE TABLE `system_param`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '自增id',
  `param_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组',
  `param_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '键',
  `param_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '键名称',
  `create_at` datetime(3) NOT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `status` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `realname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户姓名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '电话',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '职位',
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司',
  `industry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '行业',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mobile`(`mobile`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户信息，包括申请试用信息' ROW_FORMAT = Dynamic;

INSERT INTO user (id, username, password, status, realname, email, mobile, position, company, industry, create_at, update_at)
values (1,'admin','$2a$10$eeMrQLdbu6HaP.91n40rueeEg5aVjKeeGxm6EG8Xei2fmfDrB7sFy',1,'管理员','','admin','plantdata','','',now(),now());

-- ----------------------------
-- Table structure for user_apk
-- ----------------------------
DROP TABLE IF EXISTS `user_apk`;
CREATE TABLE `user_apk`  (
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `apk` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL  COMMENT 'APK',
  `is_enabled` tinyint(1) UNSIGNED NOT NULL DEFAULT 1,
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_apk`(`apk`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户apk信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_limit
-- ----------------------------
DROP TABLE IF EXISTS `user_limit`;
CREATE TABLE `user_limit`  (
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `dataset_count` int(11) NULL DEFAULT NULL COMMENT '数据集数量',
  `graph_count` int(11) NULL DEFAULT NULL COMMENT '图谱数量',
  `task_count` int(11) NULL DEFAULT NULL COMMENT '任务数量',
  `is_shareable` tinyint(1) NULL DEFAULT NULL COMMENT '是否可分享',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户数量限制' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_product
-- ----------------------------
DROP TABLE IF EXISTS `user_product`;
CREATE TABLE `user_product`  (
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `product_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '子系统名称',
  `status` int(10) UNSIGNED NOT NULL  COMMENT '状态 1:审核中, 2:审核通过, 3:审核拒绝 4:已过期',
  `expire_at` datetime(3) NULL DEFAULT NULL COMMENT '过期时间',
  `create_at` datetime(3) NOT NULL COMMENT '申请时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`, `product_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户产品状态' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;




