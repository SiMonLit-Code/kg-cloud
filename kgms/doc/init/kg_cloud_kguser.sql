CREATE database if NOT EXISTS `kg_cloud_kguser` default character set utf8mb4 collate utf8mb4_bin;
use `kg_cloud_kguser`;

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
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

INSERT INTO `system_param`(`id`, `param_group`, `param_key`, `param_value`, `remark`, `create_at`, `update_at`) VALUES (1, 'image', 'icoImg', 'data:img/jpg;base64,AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACKswAAirMAMoqzAJeKswDZirMA+YqzAPmKswDZirMAl4qzADKKswAAAAAAAAAAAAAAAAAAAAAAAAAAAACKswAMirMAmYqzAPuKswD/irMA/4qzAP+KswD/irMA/4qzAP+KswD7irMAmYqzAAwAAAAAAAAAAAAAAACKswAMirMAvYqzAP+KswD/irMA/5O5Ff+Qtw7/lboZ/461Cf+KswD/irMA/4qzAP+KswC9irMADAAAAACKswAAirMAl4qzAP+KswD/irMA/7rSaf++1HH/i7MC/5G3EP/P4Jf/qMZC/4qzAP+KswD/irMA/4mzAJmKswAAirMAMIqzAPuKswD/nb8q/7HMVv/a57D/irMB/6PDNv+ZvSH/kbgR/+zy1v+tyk3/kbcP/4qzAP+KswD7irMAMoqzAJeKswD/jbUH/9Hhmv/h677/o8M4/6rHRv/g67z/5O3E/5m9Iv/F2YD/0OGa/8zdkP+KswD/irMA/4qzAJeKswDZirMA/6vISf/S4Z3/7vPZ/9vosf/o8M7/wNZ3/8nciv/k7cX/5+/L/9HhnP/i7MH/krgT/4qzAP+KswDZibIA+YqzAP/E2H//9Pjo/+Ttxf/9/fv//v7+/9/quv/q8dH//v7+//b57P/f6rv/9fjq/6LCNf+KswD/irMA+YmyAPmKswD/kbcQ/7bPYP/t89n//v79///////P35b/3um3///////+/vz/3uq4/6bFPv+MtAT/irMA/4qyAPmKswDZirMA/63KTv/w9d//vdRw//T45//0+On/2ueu/9zos//5+/L/6fHQ/8bZgv/1+On/lboZ/4qzAP+KswDZirMAl4qzAP+UuRb/x9qG/9flqP/C2Hz/wdd5/8rcjP/b57H/qcdE/9rnrv/C13v/0uGc/4uzA/+KswD/irMAl4qzADKKswD7irMA/6bFPv/c6LP/ss1Y/4uzAv/C13r/scxW/4qzAP/L3Y7/ytyM/5y+J/+KswD/irMA+4qzADKKswAAirMAmYqzAP+KswD/lboY/9vnsP+avST/irMA/4qzAP+ryEj/0OCa/4y0Bf+KswD/irMA/4qzAJmKswAAAAAAAIqzAAyKswC9irMA/4qzAP+NtQj/o8M2/4qzAf+Ptgz/ocIz/4qzAf+KswD/irMA/4qzAL2KswAMAAAAAAAAAAAAAAAAirIADIqzAJmKswD7irMA/4qzAP+KswD/irMA/4qzAP+KswD/irMA+4qzAJmKswAMAAAAAAAAAAAAAAAAAAAAAAAAAACKswAAirMAMoqzAJeKswDZirMA+YqzAPmKsgDZirMAl4qzADKKswAAAAAAAAAAAAAAAAAA+B8AAOAHAADAAwAAgAEAAIABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAQAAgAEAAMADAADgBwAA+B8AAA==', 'bg image', now(), now());

INSERT INTO `system_param`(`id`, `param_group`, `param_key`, `param_value`, `remark`, `create_at`, `update_at`) VALUES (2, 'image', 'logoImg', 'data:img/jpg;base64,iVBORw0KGgoAAAANSUhEUgAAAKAAAAA4CAYAAAB6+vMDAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA4ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo1MTA0NmUwZC0zZGJkLTQ3NjUtYmRmNC0zMTAzNWI0Y2MxZWYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEVCODY2RDM0ODVFMTFFOEIwNDFCOTFCRkNBREFBM0UiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MEVCODY2RDI0ODVFMTFFOEIwNDFCOTFCRkNBREFBM0UiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpkYTA3YWJjZi1lNDQ2LTQxMzUtOWFhOC1iZjQxMjFjYmE1ZGMiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo5NjA4ZmZhNC02YmU3LTExN2ItYTky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', 'logo image', now(), now());

INSERT INTO `system_param`(`id`, `param_group`, `param_key`, `param_value`, `remark`, `create_at`, `update_at`) VALUES (3, 'theme', 'systemTheme', '#5677FC', 'system theme', now(), now());

INSERT INTO `system_param`(`id`, `param_group`, `param_key`, `param_value`, `remark`, `create_at`, `update_at`) VALUES (4, 'theme', 'loginTheme', '#00B38A', 'login theme', now(), now());

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
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统参数' ROW_FORMAT = Dynamic;

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
