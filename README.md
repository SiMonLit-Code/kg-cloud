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

