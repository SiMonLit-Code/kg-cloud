### 2020年7月9日 3.4.x
- 导航：kgtext
```
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = 'NLP Lab', `type` = 'SUB_SYSTEM', `group_id` = 11, `state` = 1, `rank` = 1, `remark` = 'NLP模型库，提供分词、命名实体识别、属性抽取、关系抽取、实体链接、关键词抽取、文本摘要生成等预构建模型。', `config` = '{\"url\":[{\"string\":\"/kgtext\"}]}', `check_configs` = '[{\"checkType\":\"CONSUL\",\"content\":\"kgtext\"}]' WHERE `id` = 11001;
```
### 2020年7月7日 3.4.x
- 导航：反向同步
```
INSERT INTO `kg_cloud_kguser`.`system_menu`(`id`, `p_id`, `title`, `is_enable`, `is_checked`, `menu_type`, `rank`, `config`, `create_at`, `update_at`) VALUES (54, 5, '反向同步', 1, 1, 'iframe', 5, '{\"routeName\":\"KgIfarme\",\"listDetail\":true,\"params\":{\"url\":[{\"string\":\"plugins/script/synclog/#/home\"},{\"fromVuex\":\"kgName\"},{\"string\":\"synclog\"}]},\"isDefault\":false}', '2020-06-24 16:11:10.271', '2020-07-06 21:49:22.036');
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = '反向同步', `type` = 'DATA', `group_id` = 1001, `state` = 1, `rank` = 1, `remark` = '当通过数仓入图的数据在图谱上进行修改时，支持将数据变更同步回原业务系统。支持新建进行反向同步计算任务，查询待反向同步的数据。', `config` = '{}', `check_configs` = '[{\"checkType\":\"CONSUL\",\"content\":\"kgsynclog\"}]' WHERE `id` = 1001005;
```

### 2020年7月6日 3.4.x
- 导航：文案更新
```
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = '多图融合', `type` = 'DATA', `group_id` = 1001, `state` = 1, `rank` = 1, `remark` = '针对多张图谱的模式、数据融合任务，其包括实体及其属性、关系、权重、置信度等信息的迁移。', `config` = '{}', `check_configs` = '[{\"checkType\":\"CONSUL\",\"content\":\"kgmerge\"},{\"checkType\":\"FILE\",\"content\":\"/work/azkaban_shell/graph_merge.sh\"}]' WHERE `id` = 1001003;
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = 'Wrapper', `type` = 'DATA', `group_id` = 1001, `state` = 1, `rank` = 1, `remark` = '半结构化数据解析（Wrapper）提供可视化组件对半结构化数据如 html、xml 等进⾏行行解析配置，转换成结构化数据。', `config` = '{}', `check_configs` = '[{\"checkType\":\"FILE\",\"content\":\"/work/azkaban_shell/wrapper.sh\"}]' WHERE `id` = 1001004;
INSERT INTO `kg_cloud_kgms`.`repo_item`(`id`, `name`, `type`, `group_id`, `state`, `rank`, `remark`, `config`, `check_configs`) VALUES (1002004, 'GBase', 'DATA', 1002, 1, 1, 'Gbase数据接入驱动，支持Gbase版本为8A。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgdw\"}]');
UPDATE `kg_cloud_kguser`.`system_menu` SET `p_id` = 5, `title` = '反向同步', `is_enable` = 1, `is_checked` = 1, `menu_type` = 'iframe', `rank` = 5, `config` = '{\"routeName\":\"KgIfarme\",\"listDetail\":true,\"params\":{\"url\":[{\"string\":\"plugins/script/synclog/#/home\"},{\"fromVuex\":\"kgName\"},{\"string\":\"synclog\"}]},\"isDefault\":false}', `create_at` = '2020-06-24 16:11:10.271', `update_at` = '2020-07-06 21:49:22.036' WHERE `id` = 54;
INSERT INTO `kg_cloud_kgms`.`repo_menu`(`menu_id`, `repository_id`) VALUES (54, 1001005);
INSERT INTO `kg_cloud_kgms`.`repo_item`(`id`, `name`, `type`, `group_id`, `state`, `rank`, `remark`, `config`, `check_configs`) VALUES (1001005, '反向同步', 'DATA', 1001, 1, 1, '当通过数仓入图的数据在图谱上进行修改时，支持将数据变更同步回原业务系统。支持新建进行反向同步计算任务，查询待反向同步的数据。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"synclog\"}]');
```

### 2020年7月4日 3.4.x
- 导航：kgsearch跳转配置更新
```
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = 'KGSearch', `type` = 'SUB_SYSTEM', `group_id` = 2, `state` = 1, `rank` = 1, `remark` = '文本搜索引擎，提供基础的搜索能力，可通过增强软件包扩展搜索能力。', `config` = '{\"url\":[{\"string\":\"/kgsearch\"}]}', `check_configs` = '[{\"checkType\":\"CONSUL\",\"content\":\"kgsearch\"}]' WHERE `id` = 2001;
```
### 2020年6月29日 3.4.x
- 神经推理导航
```
UPDATE `kg_cloud_kgms`.`repo_item` SET `name` = '神经网络推理计算软件包', `type` = 'DATA', `group_id` = 902, `state` = 1, `rank` = 1, `remark` = '集成基于神经网络的推理模型，支持可视化配置神经网络任务、实时跟踪推理任务执行情况、查看推理结果并选择性入图。', `config` = '{}', `check_configs` = '[{\"checkType\":\"FILE\",\"content\":\"/work/azkaban_shell/nn-reasoning.sh\"},{\"checkType\":\"CONSUL\",\"content\":\"kgnnreasoning\"}]' WHERE `id` = 902005;
```

- 知识应用导航排序更新
```
UPDATE `kg_cloud_kguser`.`system_menu` SET `p_id` = 12, `title` = '编辑视图', `is_enable` = 1, `is_checked` = 1, `menu_type` = 'menu', `rank` = 1, `config` = '{\"listDetail\":true,\"routeName\":\"KgApplySinglePage\",\"params\":{\"kgName\":{\"value\":\"fromVuex\",\"isReal\":true},\"apk\":{\"value\":\"fromVuex\",\"isReal\":true},\"pageType\":{\"value\":\"editor\",\"isReal\":true}},\"isDefault\":false,\"icon\":\"ic-zsyy-tpksh\"}', `create_at` = '2020-05-22 13:19:35.820', `update_at` = '2020-06-28 17:49:07.253' WHERE `id` = 1200;
UPDATE `kg_cloud_kguser`.`system_menu` SET `p_id` = 12, `title` = '图谱探索', `is_enable` = 1, `is_checked` = 1, `menu_type` = 'menu', `rank` = 2, `config` = '{\"listDetail\":true,\"routeName\":\"KgApplySinglePage\",\"params\":{\"kgName\":{\"value\":\"fromVuex\",\"isReal\":true},\"apk\":{\"value\":\"fromVuex\",\"isReal\":true},\"pageType\":{\"value\":\"graph\",\"isReal\":true}},\"isDefault\":true,\"icon\":\"ic-zsyy-tts\"}', `create_at` = '2020-05-22 13:19:35.818', `update_at` = '2020-06-28 17:49:07.252' WHERE `id` = 1201;
```

### 2020年6月28日 3.4.x
- wrapper菜单配置更新
```
INSERT INTO `kg_cloud_kgms`.`repo_item`(`id`, `name`, `type`, `group_id`, `state`, `rank`, `remark`, `config`, `check_configs`) VALUES (1001004, 'Wrapper', 'DATA', 1001, 1, 1, 'Wrapper', '{}', '[{\"checkType\":\"FILE\",\"content\":\"/work/azkaban_shell/wrapper.sh\"}]');
```

- 可视化菜单配置
```
INSERT INTO `kg_cloud_kguser`.`system_menu`(`id`, `p_id`, `title`, `is_enable`, `is_checked`, `menu_type`, `rank`, `config`, `create_at`, `update_at`) VALUES (1227, 122, '可视化配置', 1, 1, 'menu', 2, '{\"routeName\":\"KgApplyVisualSet\",\"params\":{\"kgName\":{\"value\":\"fromVuex\",\"isReal\":true},\"apk\":{\"value\":\"fromVuex\",\"isReal\":true}},\"isDefault\":false}', '2020-06-28 10:24:49.095', '2020-06-28 17:49:07.281');
INSERT INTO `kg_cloud_kgms`.`repo_base_menu`(`menu_id`, `repository_id`) VALUES (1227, 0);
```

### 2020年6月24日 3.4.x 
- 多图融合菜单sql变更
```
INSERT INTO `kg_cloud_kgms`.`repo_menu`(`menu_id`, `repository_id`) VALUES (23, 1001003);
UPDATE `kg_cloud_kgms`.`repo_menu` SET `repository_id` = 1001003 WHERE `menu_id` = 230;
UPDATE `kg_cloud_kgms`.`repo_menu` SET `repository_id` = 1001003 WHERE `menu_id` = 231;
```
- nn推理菜单sql变更
```
UPDATE `kg_cloud_kgms`.`repo_menu` SET `repository_id` = 902004 WHERE `menu_id` = 148;
INSERT INTO `kg_cloud_kgms`.`repo_menu`(`menu_id`, `repository_id`) VALUES (149, 902005);
```

