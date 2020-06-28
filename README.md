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

