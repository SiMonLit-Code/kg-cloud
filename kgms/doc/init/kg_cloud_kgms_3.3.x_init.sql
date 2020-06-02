
CREATE database if NOT EXISTS `kg_cloud_kgms` default character set utf8mb4 collate utf8mb4_bin;
use `kg_cloud_kgms`;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_audit
-- ----------------------------
DROP TABLE IF EXISTS `api_audit`;
CREATE TABLE `api_audit`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '访问地址',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱名称',
  `page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'SPA 页面 开发者',
  `status` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '0:失败，1:成功',
  `invoke_at` datetime(3) NULL DEFAULT NULL COMMENT '调用时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_url`(`url`) USING BTREE,
  INDEX `idx_kg_name`(`kg_name`) USING BTREE,
  INDEX `idx_invoke_time`(`invoke_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'api 接口访问' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '词典库名称',
  `db_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '词汇集',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000000461 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '领域词典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_folder
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件夹名称',
  `file_system_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '文件系统id',
  `is_default` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件系统管理-文件夹' ROW_FORMAT = Dynamic;
-- ----------------------------
-- Table structure for file_system
-- ----------------------------
DROP TABLE IF EXISTS `file_system`;
CREATE TABLE `file_system`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件系统名称',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `is_default` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件系统管理-文件系统' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph
-- ----------------------------
DROP TABLE IF EXISTS `graph`;
CREATE TABLE `graph`  (
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱标识',
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据库',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱图片',
  `is_privately` tinyint(1) NULL DEFAULT NULL COMMENT '是否为个人',
  `is_editable` tinyint(1) NULL DEFAULT NULL COMMENT '是否允许编辑',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除标志位',
  `is_vkg` tinyint(1) NULL DEFAULT NULL COMMENT '是否是虚拟图谱',
  `vkg_mapping` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '虚拟图谱映射配置',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`kg_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_attr_group
-- ----------------------------
DROP TABLE IF EXISTS `graph_attr_group`;
CREATE TABLE `graph_attr_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱名称',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性分组名称',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004105747 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱属性分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_attr_group_details
-- ----------------------------
DROP TABLE IF EXISTS `graph_attr_group_details`;
CREATE TABLE `graph_attr_group_details`  (
  `attr_id` bigint(20) NOT NULL COMMENT '属性id',
  `group_id` bigint(20) NOT NULL COMMENT '属性分组id',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`attr_id`, `group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱属性与属性分组关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_attr_quality
-- ----------------------------
DROP TABLE IF EXISTS `graph_attr_quality`;
CREATE TABLE `graph_attr_quality`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱kgName',
  `self_id` bigint(11) NULL DEFAULT NULL COMMENT '概念id',
  `attr_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性名称',
  `attr_count` int(11) NULL DEFAULT NULL COMMENT '属性值数量',
  `attr_integrity` double(11, 2) NULL DEFAULT NULL COMMENT '属性模式完整度',
  `attr_reliability` double(11, 2) NULL DEFAULT NULL COMMENT '属性置信度',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1421083 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_attr_template
-- ----------------------------
DROP TABLE IF EXISTS `graph_attr_template`;
CREATE TABLE `graph_attr_template`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板名称',
  `abs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板简介',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模板配置',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱属性模板' ROW_FORMAT = Dynamic;


INSERT INTO `graph_attr_template` VALUES (1, '人物', '人物属性包含出生日期、性别、身高、国籍、民族、学位、邮箱、电话号码等属性。', '[{\"name\": \"出生日期\",\"type\": 0,\"dataType\": 4}, {\"name\": \"性别\",\"type\": 0,\"dataType\": 5}, {\"name\": \"身高\",\"type\": 0,\"dataType\": 2}, {\"name\": \"国籍\",\"type\": 0,\"dataType\": 5}, {\"name\": \"民族\",\"type\": 0,\"dataType\": 5}, {\"name\": \"学位\",\"type\": 0,\"dataType\": 5}, {\"name\": \"邮箱\",\"type\": 0,\"dataType\": 5}, {\"name\": \"电话号码\",\"type\": 0,\"dataType\": 5}, {\"name\": \"主要成就\",\"type\": 0,\"dataType\": 5}, {\"name\": \"出生地\",\"type\": 1,\"range\": [{\"name\": \"地点\"}]}, {\"name\": \"毕业院校\",\"type\": 1,\"range\": [{\"name\": \"学校\"}, {\"name\": \"机构\"}]}, {\"name\": \"工作单位\",\"type\": 1,\"range\": [{\"name\": \"机构\"}]}, {\"name\": \"子女\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"父亲\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"母亲\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"哥哥\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"弟弟\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"姐姐\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"妹妹\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}, {\"name\": \"配偶\",\"type\": 1,\"range\": [{\"name\": \"人物\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (2, '机构', '机构属性包含地址、成立日期、联系电话、传真、电子邮件等属性。', '[{\"name\":\"地址\",\"type\":0,\"dataType\":5},{\"name\":\"成立日期\",\"type\":0,\"dataType\":41},{\"name\":\"联系电话\",\"type\":0,\"dataType\":5},{\"name\":\"传真\",\"type\":0,\"dataType\":5},{\"name\":\"电子邮件\",\"type\":0,\"dataType\":5},{\"name\":\"领导人\",\"type\":1,\"range\":[{\"name\":\"人物\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (3, '院校', '院校属性包含创办时间、英文名称、类型、类型、校训、校歌、院校代码等属性。', '[{\"name\":\"创办时间\",\"type\":0,\"dataType\":4},{\"name\":\"英文名称\",\"type\":0,\"dataType\":5},{\"name\":\"类别\",\"type\":0,\"dataType\":5},{\"name\":\"类型\",\"type\":0,\"dataType\":5},{\"name\":\"属性\",\"type\":0,\"dataType\":5},{\"name\":\"校训\",\"type\":0,\"dataType\":5},{\"name\":\"校歌\",\"type\":0,\"dataType\":5},{\"name\":\"地址\",\"type\":0,\"dataType\":5},{\"name\":\"院校代码\",\"type\":0,\"dataType\":5},{\"name\":\"主管部门\",\"type\":1,\"range\":[{\"name\":\"机构\"}]},{\"name\":\"现任领导\",\"type\":1,\"range\":[{\"name\":\"人物\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (4, '国家', '国家属性包含简称、英文名称、中文名称、国歌、国家代码、语言、人口、国土面积等属性。', '[{\"name\":\"简称\",\"type\":0,\"dataType\":5},{\"name\":\"英文名称\",\"type\":0,\"dataType\":5},{\"name\":\"中文名称\",\"type\":0,\"dataType\":5},{\"name\":\"国歌\",\"type\":0,\"dataType\":5},{\"name\":\"国家代码\",\"type\":0,\"dataType\":5},{\"name\":\"语言\",\"type\":0,\"dataType\":5},{\"name\":\"货币\",\"type\":0,\"dataType\":5},{\"name\":\"人口\",\"type\":0,\"dataType\":2},{\"name\":\"国土面积\",\"type\":0,\"dataType\":2},{\"name\":\"GDP\",\"type\":0,\"dataType\":2},{\"name\":\"首都\",\"type\":1,\"range\":[{\"name\":\"地点\"}]},{\"name\":\"所在洲\",\"type\":1,\"range\":[{\"name\":\"地点\"}]},{\"name\":\"国家领导人\",\"type\":1,\"range\":[{\"name\":\"人物\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (5, '地点', '地点属性包含中文名称、英文名称、地理位置、建筑面积始建时间、气候等属性。', '[{\"name\":\"中文名称\",\"type\":0,\"dataType\":5},{\"name\":\"英文名称\",\"type\":0,\"dataType\":5},{\"name\":\"地理位置\",\"type\":0,\"dataType\":5},{\"name\":\"建筑面积\",\"type\":0,\"dataType\":2},{\"name\":\"始建时间\",\"type\":0,\"dataType\":5},{\"name\":\"气候\",\"type\":0,\"dataType\":5}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (6, '公司', '公司属性包含中文名称、英文名称、成立时间、公司类型、年营业额、员工数量、经营范围等属性。', '[{\"name\":\"中文名称\",\"type\":0,\"dataType\":5},{\"name\":\"英文名称\",\"type\":0,\"dataType\":5},{\"name\":\"成立时间\",\"type\":0,\"dataType\":4},{\"name\":\"公司类型\",\"type\":0,\"dataType\":5},{\"name\":\"年营业额\",\"type\":0,\"dataType\":2},{\"name\":\"员工数量\",\"type\":0,\"dataType\":1},{\"name\":\"公司使命\",\"type\":0,\"dataType\":5},{\"name\":\"经营范围\",\"type\":0,\"dataType\":5},{\"name\":\"注册号\",\"type\":0,\"dataType\":5},{\"name\":\"注册资本\",\"type\":0,\"dataType\":2},{\"name\":\"总部地点\",\"type\":1,\"range\":[{\"name\":\"地点\"}]},{\"name\":\"创始人\",\"type\":1,\"range\":[{\"name\":\"人物\"}]},{\"name\":\"首席执行官\",\"type\":1,\"range\":[{\"name\":\"人物\"}]},{\"name\":\"登记机关\",\"type\":1,\"range\":[{\"name\":\"机构\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (7, '专利', '专利属性包含申请号、申请日、申请人、公开号、专利摘要、发明人、代理人等属性。', '[{\"name\":\"申请号\",\"type\":0,\"dataType\":5},{\"name\":\"申请日\",\"type\":0,\"dataType\":4},{\"name\":\"申请人\",\"type\":0,\"dataType\":5},{\"name\":\"公开号\",\"type\":0,\"dataType\":5},{\"name\":\"首次公开号\",\"type\":0,\"dataType\":5},{\"name\":\"专利摘要\",\"type\":0,\"dataType\":10},{\"name\":\"发明人\",\"type\":1,\"range\":[{\"name\":\"人物\"}]},{\"name\":\"代理人\",\"type\":1,\"range\":[{\"name\":\"人物\"}]},{\"name\":\"代理机构\",\"type\":1,\"range\":[{\"name\":\"机构\"}]}]', now(), now());
INSERT INTO `graph_attr_template` VALUES (8, '论文', '论文属性包含题目、来源、作者、发表时间、分类号等属性。', '[{\"name\":\"题目\",\"type\":0,\"dataType\":5},{\"name\":\"来源\",\"type\":0,\"dataType\":5},{\"name\":\"发表时间\",\"type\":0,\"dataType\":4},{\"name\":\"数据库\",\"type\":0,\"dataType\":5},{\"name\":\"分类号\",\"type\":0,\"dataType\":5},{\"name\":\"作者\",\"type\":1,\"range\":[{\"name\":\"人物\"}]}]', now(), now());


-- ----------------------------
-- Table structure for graph_conf_algorithm
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_algorithm`;
CREATE TABLE `graph_conf_algorithm`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `algorithm_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法名称',
  `algorithm_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法请求路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法描述',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004105976 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱业务算法配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_focus
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_focus`;
CREATE TABLE `graph_conf_focus`  (
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `focus_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '初始化图谱类型',
  `focus_entity` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '默认节点',
  `focus_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱默认配置',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`kg_name`, `focus_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱默认焦点配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_kgql
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_kgql`;
CREATE TABLE `graph_conf_kgql`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名',
  `rule_type` int(11) NOT NULL DEFAULT 0 COMMENT '规则类型 0 search 1 gis',
  `kgql_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规则名称',
  `kgql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'kgql源数据',
  `rule_settings` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图探索规则',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004105675 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图探索规则配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_qa
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_qa`;
CREATE TABLE `graph_conf_qa`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图名',
  `qa_type` int(11) NOT NULL DEFAULT 1 COMMENT '0:概念，1：实体',
  `question` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '问题模板',
  `count` int(11) NOT NULL COMMENT '替换的实体个数',
  `concept_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '父概念id',
  `priority` int(11) NULL DEFAULT 0 COMMENT '优先级，越大越优先',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000005661 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱qa问答模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_qa_status
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_qa_status`;
CREATE TABLE `graph_conf_qa_status`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '问答提示状态(0：关闭，1：开启)',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱qa问答设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_reasoning
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_reasoning`;
CREATE TABLE `graph_conf_reasoning`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `rule_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '推理规则名称',
  `rule_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '推理规则配置',
  `rule_settings` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '推理规则设置',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100001770347 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱推理规则配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_conf_statistical
-- ----------------------------
DROP TABLE IF EXISTS `graph_conf_statistical`;
CREATE TABLE `graph_conf_statistical`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `statis_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱统计类型',
  `statis_rule` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '统计规则',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004107648 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱统计配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for graph_quality
-- ----------------------------
DROP TABLE IF EXISTS `graph_quality`;
CREATE TABLE `graph_quality`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '概念id',
  `self_id` bigint(20) NULL DEFAULT NULL COMMENT '当前概念id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '概念名称',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱kgName',
  `concept_id` bigint(20) NULL DEFAULT NULL COMMENT '父概念id',
  `entity_count` bigint(20) NULL DEFAULT NULL COMMENT '实体数量(当前概念下的实体)',
  `entity_total` bigint(20) NULL DEFAULT NULL COMMENT '实体总数(包含子概念下的实体)',
  `attr_definition_count` int(11) NULL DEFAULT NULL COMMENT '属性定义数量',
  `schema_integrity` double(11, 2) NULL DEFAULT NULL COMMENT '模式完整度',
  `reliability` double(11, 2) NULL DEFAULT NULL COMMENT '知识质量置信度',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `kg_name_id`(`self_id`, `kg_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 640002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱质量统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for link_share
-- ----------------------------
DROP TABLE IF EXISTS `link_share`;
CREATE TABLE `link_share`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱名称',
  `spa_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单页应用id',
  `share_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分享链接',
  `is_shared` tinyint(1) NULL DEFAULT NULL COMMENT '是否分享',
  `total_scan` bigint(20) NULL DEFAULT 0 COMMENT '总浏览量',
  `expire_at` datetime(3) NULL DEFAULT NULL COMMENT '过期时间',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_kg_name_spa_id`(`kg_name`, `spa_id`) USING BTREE,
  UNIQUE INDEX `idx_share_link`(`share_link`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000000520 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '链接分享' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu_favor
-- ----------------------------
DROP TABLE IF EXISTS `menu_favor`;
CREATE TABLE `menu_favor`  (
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `menu_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '菜单id',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单订阅' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prebuild_attr
-- ----------------------------
DROP TABLE IF EXISTS `prebuild_attr`;
CREATE TABLE `prebuild_attr`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性名',
  `concept_id` int(11) NULL DEFAULT NULL COMMENT '定义域概念id',
  `attr_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '唯一标识',
  `attr_type` int(11) NULL DEFAULT NULL COMMENT '属性类型 1对象 0数值',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数值属性属性值类型',
  `range_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值域名称',
  `range_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值域概念id',
  `alias` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '属性别名',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性单位',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '关联的模式id',
  `tables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '引用的表名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5979 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '预构建模式属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prebuild_concept
-- ----------------------------
DROP TABLE IF EXISTS `prebuild_concept`;
CREATE TABLE `prebuild_concept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '概念名称',
  `meaning_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消歧标识',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片地址',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父概念id',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `concept_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '唯一标识',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '所属模式id',
  `tables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '引用的表名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2007 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '预构建模式概念' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prebuild_model
-- ----------------------------
DROP TABLE IF EXISTS `prebuild_model`;
CREATE TABLE `prebuild_model`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱名称',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '预构建模式状态 0禁用  1发布 2失效',
  `model_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模式类别',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '所属用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发布用户名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  `permission` int(11) NULL DEFAULT NULL COMMENT '权限状态 1公有  0私有',
  `is_standard_template` tinyint(1) NULL DEFAULT NULL COMMENT '是否为行业标准模板 1是 0不是',
  `database_id` bigint(20) NULL DEFAULT NULL COMMENT '数仓数据库id',
  `ktr` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '清洗ktr文件，行业标准模板才有',
  `file_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '样例文件内容（json）',
  `tag_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模式定义',
  `schemas` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模式表schema',
  `yaml_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模式对应yaml文件',
  `quote_count` int(11) NULL DEFAULT NULL COMMENT '引用数量',
  `table_labels` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '自定义打标模式',
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱kgName',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 512 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '预构建模式' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prebuild_relation_attr
-- ----------------------------
DROP TABLE IF EXISTS `prebuild_relation_attr`;
CREATE TABLE `prebuild_relation_attr`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '边属性名称',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '边属性类型',
  `attr_id` int(11) NULL DEFAULT NULL COMMENT '边属性所属对象属性id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '关联的模式id',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单位',
  `tables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '映射的表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 499 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '预构建关系属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reasoning
-- ----------------------------
DROP TABLE IF EXISTS `reasoning`;
CREATE TABLE `reasoning`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图谱名',
  `config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '推理配置',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推理规则名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '在线推理配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repo_base_menu
-- ----------------------------
DROP TABLE IF EXISTS `repo_base_menu`;
CREATE TABLE `repo_base_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `repository_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件基础菜单表' ROW_FORMAT = Dynamic;

INSERT INTO `repo_base_menu` VALUES (1, 0);
INSERT INTO `repo_base_menu` VALUES (4, 0);
INSERT INTO `repo_base_menu` VALUES (5, 0);
INSERT INTO `repo_base_menu` VALUES (9, 0);
INSERT INTO `repo_base_menu` VALUES (12, 0);
INSERT INTO `repo_base_menu` VALUES (13, 0);
INSERT INTO `repo_base_menu` VALUES (14, 0);
INSERT INTO `repo_base_menu` VALUES (16, 0);
INSERT INTO `repo_base_menu` VALUES (19, 0);
INSERT INTO `repo_base_menu` VALUES (20, 0);
INSERT INTO `repo_base_menu` VALUES (21, 0);
INSERT INTO `repo_base_menu` VALUES (30, 0);
INSERT INTO `repo_base_menu` VALUES (40, 0);
INSERT INTO `repo_base_menu` VALUES (42, 0);
INSERT INTO `repo_base_menu` VALUES (50, 0);
INSERT INTO `repo_base_menu` VALUES (52, 0);
INSERT INTO `repo_base_menu` VALUES (53, 0);
INSERT INTO `repo_base_menu` VALUES (70, 0);
INSERT INTO `repo_base_menu` VALUES (90, 0);
INSERT INTO `repo_base_menu` VALUES (91, 0);
INSERT INTO `repo_base_menu` VALUES (92, 0);
INSERT INTO `repo_base_menu` VALUES (93, 0);
INSERT INTO `repo_base_menu` VALUES (94, 0);
INSERT INTO `repo_base_menu` VALUES (96, 0);
INSERT INTO `repo_base_menu` VALUES (97, 0);
INSERT INTO `repo_base_menu` VALUES (122, 0);
INSERT INTO `repo_base_menu` VALUES (123, 0);
INSERT INTO `repo_base_menu` VALUES (131, 0);
INSERT INTO `repo_base_menu` VALUES (132, 0);
INSERT INTO `repo_base_menu` VALUES (135, 0);
INSERT INTO `repo_base_menu` VALUES (138, 0);
INSERT INTO `repo_base_menu` VALUES (139, 0);
INSERT INTO `repo_base_menu` VALUES (142, 0);
INSERT INTO `repo_base_menu` VALUES (143, 0);
INSERT INTO `repo_base_menu` VALUES (145, 0);
INSERT INTO `repo_base_menu` VALUES (160, 0);
INSERT INTO `repo_base_menu` VALUES (161, 0);
INSERT INTO `repo_base_menu` VALUES (162, 0);
INSERT INTO `repo_base_menu` VALUES (163, 0);
INSERT INTO `repo_base_menu` VALUES (164, 0);
INSERT INTO `repo_base_menu` VALUES (165, 0);
INSERT INTO `repo_base_menu` VALUES (166, 0);
INSERT INTO `repo_base_menu` VALUES (192, 0);
INSERT INTO `repo_base_menu` VALUES (410, 0);
INSERT INTO `repo_base_menu` VALUES (920, 0);
INSERT INTO `repo_base_menu` VALUES (921, 0);
INSERT INTO `repo_base_menu` VALUES (922, 0);
INSERT INTO `repo_base_menu` VALUES (923, 0);
INSERT INTO `repo_base_menu` VALUES (925, 0);
INSERT INTO `repo_base_menu` VALUES (926, 0);
INSERT INTO `repo_base_menu` VALUES (928, 0);
INSERT INTO `repo_base_menu` VALUES (929, 0);
INSERT INTO `repo_base_menu` VALUES (930, 0);
INSERT INTO `repo_base_menu` VALUES (931, 0);
INSERT INTO `repo_base_menu` VALUES (932, 0);
INSERT INTO `repo_base_menu` VALUES (933, 0);
INSERT INTO `repo_base_menu` VALUES (940, 0);
INSERT INTO `repo_base_menu` VALUES (941, 0);
INSERT INTO `repo_base_menu` VALUES (942, 0);
INSERT INTO `repo_base_menu` VALUES (943, 0);
INSERT INTO `repo_base_menu` VALUES (944, 0);
INSERT INTO `repo_base_menu` VALUES (964, 0);
INSERT INTO `repo_base_menu` VALUES (965, 0);
INSERT INTO `repo_base_menu` VALUES (966, 0);
INSERT INTO `repo_base_menu` VALUES (967, 0);
INSERT INTO `repo_base_menu` VALUES (968, 0);
INSERT INTO `repo_base_menu` VALUES (969, 0);
INSERT INTO `repo_base_menu` VALUES (970, 0);
INSERT INTO `repo_base_menu` VALUES (971, 0);
INSERT INTO `repo_base_menu` VALUES (972, 0);
INSERT INTO `repo_base_menu` VALUES (973, 0);
INSERT INTO `repo_base_menu` VALUES (974, 0);
INSERT INTO `repo_base_menu` VALUES (975, 0);
INSERT INTO `repo_base_menu` VALUES (976, 0);
INSERT INTO `repo_base_menu` VALUES (977, 0);
INSERT INTO `repo_base_menu` VALUES (978, 0);
INSERT INTO `repo_base_menu` VALUES (979, 0);
INSERT INTO `repo_base_menu` VALUES (980, 0);
INSERT INTO `repo_base_menu` VALUES (981, 0);
INSERT INTO `repo_base_menu` VALUES (982, 0);
INSERT INTO `repo_base_menu` VALUES (983, 0);
INSERT INTO `repo_base_menu` VALUES (984, 0);
INSERT INTO `repo_base_menu` VALUES (985, 0);
INSERT INTO `repo_base_menu` VALUES (986, 0);
INSERT INTO `repo_base_menu` VALUES (987, 0);
INSERT INTO `repo_base_menu` VALUES (988, 0);
INSERT INTO `repo_base_menu` VALUES (1009, 0);
INSERT INTO `repo_base_menu` VALUES (1200, 0);
INSERT INTO `repo_base_menu` VALUES (1201, 0);
INSERT INTO `repo_base_menu` VALUES (1202, 0);
INSERT INTO `repo_base_menu` VALUES (1203, 0);
INSERT INTO `repo_base_menu` VALUES (1204, 0);
INSERT INTO `repo_base_menu` VALUES (1205, 0);
INSERT INTO `repo_base_menu` VALUES (1206, 0);
INSERT INTO `repo_base_menu` VALUES (1207, 0);
INSERT INTO `repo_base_menu` VALUES (1209, 0);
INSERT INTO `repo_base_menu` VALUES (1210, 0);
INSERT INTO `repo_base_menu` VALUES (1211, 0);
INSERT INTO `repo_base_menu` VALUES (1220, 0);
INSERT INTO `repo_base_menu` VALUES (1222, 0);
INSERT INTO `repo_base_menu` VALUES (1223, 0);
INSERT INTO `repo_base_menu` VALUES (1224, 0);
INSERT INTO `repo_base_menu` VALUES (1225, 0);
INSERT INTO `repo_base_menu` VALUES (1226, 0);
INSERT INTO `repo_base_menu` VALUES (1610, 0);
INSERT INTO `repo_base_menu` VALUES (1611, 0);
INSERT INTO `repo_base_menu` VALUES (10090, 0);
INSERT INTO `repo_base_menu` VALUES (10091, 0);
INSERT INTO `repo_base_menu` VALUES (10092, 0);
INSERT INTO `repo_base_menu` VALUES (10093, 0);
INSERT INTO `repo_base_menu` VALUES (16100, 0);
INSERT INTO `repo_base_menu` VALUES (16101, 0);
INSERT INTO `repo_base_menu` VALUES (16110, 0);
INSERT INTO `repo_base_menu` VALUES (16111, 0);

-- ----------------------------
-- Table structure for repo_handler
-- ----------------------------
DROP TABLE IF EXISTS `repo_handler`;
CREATE TABLE `repo_handler`  (
  `id` int(11) NOT NULL,
  `interface_id` int(11) NULL DEFAULT NULL,
  `repo_id` int(11) NULL DEFAULT NULL,
  `rank` int(11) NULL DEFAULT NULL,
  `handle_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `handle_condition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `request_server_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repo_interface
-- ----------------------------
DROP TABLE IF EXISTS `repo_interface`;
CREATE TABLE `repo_interface`  (
  `id` int(11) NOT NULL,
  `serverName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repo_item
-- ----------------------------
DROP TABLE IF EXISTS `repo_item`;
CREATE TABLE `repo_item`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组件id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组件名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DATA' COMMENT '组件类型',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `state` tinyint(1) NULL DEFAULT 1 COMMENT '1启用状态 0停用状态',
  `rank` int(11) NULL DEFAULT 1 COMMENT '排序',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '样式设置',
  `check_configs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检测配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1004003 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repo_item_group
-- ----------------------------
DROP TABLE IF EXISTS `repo_item_group`;
CREATE TABLE `repo_item_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
  `rank` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1005 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件分组' ROW_FORMAT = Dynamic;

INSERT INTO `repo_item_group` VALUES (1, 0, '自助式应用扩展包', 1, '提供数据智能编排、文本加工、Web、桌面、office助手等自助式应用');
INSERT INTO `repo_item_group` VALUES (2, 0, '搜索扩展包', 2, '提供基础搜索组件，以及NLP、图谱增强搜索、搜索指标计算等扩展能力');
INSERT INTO `repo_item_group` VALUES (3, 0, '问答扩展包', 3, '提供知识问答基础版，以及问答配置、多策略问答、日志计算等扩展能力');
INSERT INTO `repo_item_group` VALUES (4, 0, '推荐扩展包', 4, '提供基础推荐组件，以及NLP、图谱增强推荐、推荐指标计算等扩展能力');
INSERT INTO `repo_item_group` VALUES (5, 0, '图谱模式扩展包', 5, '提供通用和行业图谱模式');
INSERT INTO `repo_item_group` VALUES (6, 0, '图谱数据扩展包', 6, '提供通用和行业图谱数据');
INSERT INTO `repo_item_group` VALUES (7, 0, '业务模型扩展包', 7, '提供面向特定行业应用场景的业务模型');
INSERT INTO `repo_item_group` VALUES (8, 0, '算法模型扩展包', 8, '提供通用和面向特定行业的NLP模型');
INSERT INTO `repo_item_group` VALUES (9, 0, '图谱扩展包', 9, '提供知识图谱的模式、数据、存储、构建、计算、应用的扩展能力');
INSERT INTO `repo_item_group` VALUES (10, 0, '数仓扩展包', 10, '提供数仓基础版，以及数据驱动、接入模板等数仓扩展能力');
INSERT INTO `repo_item_group` VALUES (11, 0, '文本扩展包', 11, '提供文本抽取平台基础版，以及协同标注、在线训练、文本抽取等扩展能力');
INSERT INTO `repo_item_group` VALUES (501, 5, '通用图谱模式', 1, NULL);
INSERT INTO `repo_item_group` VALUES (502, 5, '金融图谱模式', 2, NULL);
INSERT INTO `repo_item_group` VALUES (503, 5, '军政图谱模式', 3, NULL);
INSERT INTO `repo_item_group` VALUES (601, 6, '通用图谱数据', 1, NULL);
INSERT INTO `repo_item_group` VALUES (602, 6, '军政图谱数据', 2, NULL);
INSERT INTO `repo_item_group` VALUES (701, 7, '军政业务模型包', 1, NULL);
INSERT INTO `repo_item_group` VALUES (702, 7, '金融业务模型包', 2, NULL);
INSERT INTO `repo_item_group` VALUES (801, 8, '通用算法模型包', 1, NULL);
INSERT INTO `repo_item_group` VALUES (802, 8, '军政算法模型包', 2, NULL);
INSERT INTO `repo_item_group` VALUES (803, 8, '金融算法模型包', 3, NULL);
INSERT INTO `repo_item_group` VALUES (901, 9, '图谱存储扩展包', 1, NULL);
INSERT INTO `repo_item_group` VALUES (902, 9, '图谱构建扩展包', 2, NULL);
INSERT INTO `repo_item_group` VALUES (904, 9, '图谱应用扩展包', 3, NULL);
INSERT INTO `repo_item_group` VALUES (1001, 10, '数仓能力扩展包', 1, NULL);
INSERT INTO `repo_item_group` VALUES (1002, 10, '接入驱动扩展包', 2, NULL);
INSERT INTO `repo_item_group` VALUES (1003, 10, '接入模板扩展包', 3, NULL);
INSERT INTO `repo_item_group` VALUES (1004, 10, '数仓统计扩展包', 4, NULL);

-- ----------------------------
-- Table structure for repo_menu
-- ----------------------------
DROP TABLE IF EXISTS `repo_menu`;
CREATE TABLE `repo_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `repository_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100931 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repo_use_log
-- ----------------------------
DROP TABLE IF EXISTS `repo_use_log`;
CREATE TABLE `repo_use_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `click_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  `type` tinyint(1) UNSIGNED NOT NULL COMMENT '菜单id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点击使用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_snapshot`;
CREATE TABLE `task_graph_snapshot`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图谱名称',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称_任务ID_执行ID',
  `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件大小',
  `catalogue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件保存目录',
  `file_store_type` int(11) NULL DEFAULT NULL COMMENT '文件存储类型(0：本地服务器，1：fastDFS)',
  `file_backup_type` int(11) NULL DEFAULT NULL COMMENT '文件备份类型(0：仅图谱数据，1：图谱数据和多模态数据)',
  `disk_space_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '磁盘空间总量',
  `status` int(11) NULL DEFAULT NULL COMMENT '0:进行中,1:成功，2:失败',
  `restore_at` datetime(3) NULL DEFAULT NULL COMMENT '还原时间',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '快照任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_status
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_status`;
CREATE TABLE `task_graph_status`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `task_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_at` datetime(3) NULL DEFAULT NULL,
  `update_at` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100004107053 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱任务状态' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
