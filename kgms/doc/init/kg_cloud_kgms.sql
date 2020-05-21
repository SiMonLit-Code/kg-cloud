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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin  COMMENT = '数据集管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_set
-- ----------------------------
DROP TABLE IF EXISTS `data_set`;
CREATE TABLE `data_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `folder_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '文件夹id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `data_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据集标识',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据集名称',
  `is_private` tinyint(1) NULL DEFAULT NULL COMMENT '数据集是否是私人的',
  `is_editable` tinyint(1) NULL DEFAULT NULL COMMENT '数据集是否允许编辑',
  `data_type` int(11) NULL DEFAULT NULL COMMENT '数据集存储介质 1-mongo 2-elasticsearch',
  `create_type` int(11) NULL DEFAULT NULL COMMENT '数据集创建方式 0-linking 1-selfCreate 2-File 3-Stroe 5 文本数据集 6 拼音搜索',
  `addr` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址 如: 192.168.4.11:19130,192.168.4.20:19130',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据库名',
  `tb_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表名',
  `create_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建方式',
  `fields` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '列',
  `schema_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '表结构配置',
  `mapper` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '表关系映射',
  `skill_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '技能配置',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_data_name`(`data_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_set_annotation
-- ----------------------------
DROP TABLE IF EXISTS `data_set_annotation`;
CREATE TABLE `data_set_annotation`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `dataset_id` bigint(20) UNSIGNED NOT NULL COMMENT '数据集id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '配置',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '手工标引表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_set_folder
-- ----------------------------
DROP TABLE IF EXISTS `data_set_folder`;
CREATE TABLE `data_set_folder`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `folder_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件夹名称',
  `is_defaulted` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认文件夹',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据集文件夹' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '领域词典表' ROW_FORMAT = Dynamic;

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
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`kg_name`, `user_id`) USING BTREE
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱属性分组表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱属性模板' ROW_FORMAT = Dynamic;

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
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `algorithm_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法名称',
  `algorithm_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法请求路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '算法描述',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱业务算法配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图探索规则配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱qa问答模板' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱推理规则配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱统计配置' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '链接分享' ROW_FORMAT = Dynamic;

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
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `model_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模型名称',
  `model_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模型地址',
  `model_type` int(11) NULL DEFAULT NULL COMMENT '1:实体抽取,2:属性抽取,3:同义关系抽取,4:关系抽取',
  `prf` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模型分数',
  `labels` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '模型识别标签',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模型管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_reasoning
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_reasoning`;
CREATE TABLE `task_graph_reasoning`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `rule_config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规则配置',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '推理任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_search
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_search`;
CREATE TABLE `task_graph_search`  (
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务ID',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`kg_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '搜索任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_snapshot`;
CREATE TABLE `task_graph_snapshot`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称_任务ID_执行ID',
  `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件大小',
  `status` int(11) NULL DEFAULT NULL COMMENT '0:进行中,1:成功，2:失败',
  `restore_at` datetime(3) NULL DEFAULT NULL COMMENT '还原时间',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '快照任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_graph_status
-- ----------------------------
DROP TABLE IF EXISTS `task_graph_status`;
CREATE TABLE `task_graph_status`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `kg_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图谱名称',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '参数',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务类型',
  `task_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务状态',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图谱任务状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_template
-- ----------------------------
DROP TABLE IF EXISTS `task_template`;
CREATE TABLE `task_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板',
  `create_at` datetime(3) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '任务模板' ROW_FORMAT = Dynamic;

INSERT INTO `task_template` VALUES (1, '网页数据解析-入图', '[\"wrapper\",\"d2r\"]', now(), now());
INSERT INTO `task_template` VALUES (2, '结构化数据入图-标引', '[\"d2r\",\"annotation\"]', now(), now());
INSERT INTO `task_template` VALUES (3, '网页数据解析-入图-标引', '[\"wrapper\",\"d2r\",\"annotation\"]', now(), now());
INSERT INTO `task_template` VALUES (4, '数据接入-网页数据解析-D2R', '[\"etl\",\"wrapper\",\"d2r\"]', now(), now());

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for graph_quality
-- ----------------------------
DROP TABLE IF EXISTS `graph_quality`;
CREATE TABLE `graph_quality` (
  `id` bigint(20) NOT NULL COMMENT '概念id',
  `self_id` bigint(20) DEFAULT NULL COMMENT '当前概念id',
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '概念名称',
  `kg_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图谱kgName',
  `concept_id` bigint(20) DEFAULT NULL COMMENT '父概念id',
  `entity_count` bigint(20) DEFAULT NULL COMMENT '实体数量(当前概念下的实体)',
  `entity_total` bigint(20) DEFAULT NULL COMMENT '实体总数(包含子概念下的实体)',
  `attr_definition_count` int(11) DEFAULT NULL COMMENT '属性定义数量',
  `schema_integrity` double(11,2) DEFAULT NULL COMMENT '模式完整度',
  `reliability` double(11,2) DEFAULT NULL COMMENT '知识质量置信度',
  `create_at` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `kg_name_id` (`self_id`,`kg_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='图谱质量统计';

-- ----------------------------
-- Table structure for graph_attr_quality
-- ----------------------------
DROP TABLE IF EXISTS `graph_attr_quality`;
CREATE TABLE `graph_attr_quality` (
  `id` bigint(20) NOT NULL,
  `kg_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图谱kgName',
  `self_id` bigint(11) DEFAULT NULL COMMENT '概念id',
  `attr_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性名称',
  `attr_count` int(11) DEFAULT NULL COMMENT '属性值数量',
  `attr_integrity` double(11,2) DEFAULT NULL COMMENT '属性模式完整度',
  `attr_reliability` double(11,2) DEFAULT NULL COMMENT '属性置信度',
  `create_at` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(3) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='图谱质量属性统计';

-- ----------------------------
-- Table structure for file_system
-- ----------------------------
DROP TABLE IF EXISTS `file_system`;
CREATE TABLE `file_system`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件系统名称',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件系统管理-文件系统' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for file_folder
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件夹名称',
  `file_system_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '文件系统id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文件系统管理-文件夹' ROW_FORMAT = DYNAMIC;

