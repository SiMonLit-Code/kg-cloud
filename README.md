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

