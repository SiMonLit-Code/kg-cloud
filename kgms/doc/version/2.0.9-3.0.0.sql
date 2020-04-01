-- ----------------------------
-- move to api_audit
-- ----------------------------
INSERT INTO api_audit ( id,url, kg_name, page,status, invoke_at)
SELECT id, url, kg_name, page, status, invoke_time
FROM plantdata_manage.t_sdk;

-- ----------------------------
-- move to data_set
-- ----------------------------
INSERT INTO data_set (id,user_id,data_name,title,is_private,is_editable,data_type,create_type,username,password,db_name,tb_name,create_way,fields,schema_config,mapper,skill_config,create_at,update_at,addr)
SELECT id,user_id, data_name,title,is_private,allow_edit,data_type,create_type,username,password,db_name,tb_name,create_way,fields,schema_config,mapper,skill_config,create_time,update_time,concat(ip,':',port)
FROM plantdata_manage.t_my_data;


-- ----------------------------
-- move to data_set_annotation
-- ----------------------------
INSERT INTO data_set_annotation ( id, user_id, kg_name, dataset_id, title, config, remark, create_at, update_at )
SELECT id,user_id,kg_name,data_id,name,config,description,create_time,update_time
FROM plantdata_manage.t_annotation;


-- ----------------------------
-- move to data_set_folder
-- ----------------------------
INSERT INTO data_set_folder (id,user_id,folder_name,is_defaulted,create_at,update_at)
SELECT id,user_id, file_name,type,create_time,update_time
FROM plantdata_manage.t_file_manage;

update data_set a1,plantdata_manage.t_data_file a2
set a1.folder_id = a2.file_id
where a1.id=a2.data_id;

-- ----------------------------
-- move to graph
-- ----------------------------
INSERT INTO graph (kg_name,db_name,user_id,title,icon,is_privately,is_editable,is_deleted,remark,create_at,update_at)
SELECT graph_name,'',user_id,title,icon,is_private,allow_edit,edit_view_type,remark,create_time,update_time
FROM plantdata_manage.t_my_graph;

-- ----------------------------
-- move to graph_attr_group
-- ----------------------------
INSERT INTO graph_attr_group ( id, kg_name, group_name, create_at, update_at )
SELECT id, kg_name, name,create_time,update_time
FROM plantdata_manage.t_attr_category;


-- ----------------------------
-- move to graph_attr_group_details
-- ----------------------------
INSERT INTO graph_attr_group_details (attr_id,group_id,create_at,update_at)
SELECT attr_id, category_id,create_time,update_time
FROM plantdata_manage.t_attr_category_details;


-- ----------------------------
-- move to graph_conf_algorithm
-- ----------------------------
INSERT INTO graph_conf_algorithm (kg_name,algorithm_name,algorithm_url,remark,create_at,update_at)
SELECT kg_name,name,url,abs,create_time,update_time
FROM plantdata_manage.t_graph_business_algorithm;

-- ----------------------------
-- move to graph_conf_focus
-- ----------------------------
INSERT INTO graph_conf_focus (kg_name,focus_type,focus_entity,focus_config,create_at,update_at)
SELECT kg_name,type,entities,config,create_time,update_time
FROM plantdata_manage.t_init_graph;


-- ----------------------------
-- move to graph_conf_kgql
-- ----------------------------
INSERT INTO graph_conf_kgql (kg_name,rule_type,kgql_name,kgql,rule_settings,create_at,update_at)
SELECT kg_name,rule_type,rule_name,rule_kgql,rule_settings,create_time,update_time
FROM plantdata_manage.t_graph_rule;


-- ----------------------------
-- move to graph_conf_qa
-- ----------------------------
INSERT INTO graph_conf_qa (id,kg_name,qa_type,question,count,concept_ids,priority,create_at,update_at)
SELECT  id,kg_name,type,question,count,concept_ids,priority,create_time,update_time
FROM plantdata_manage.t_qa_template;


-- ----------------------------
-- move to graph_conf_reasoning
-- ----------------------------
INSERT INTO graph_conf_reasoning (id,kg_name,rule_name,rule_config,rule_settings,create_at,update_at)
SELECT  rule_id,kg_name,rule_name,rule_config,rule_settings,create_time,update_time
FROM plantdata_manage.t_rule;


-- ----------------------------
-- move to graph_conf_statistical
-- ----------------------------
INSERT INTO graph_conf_statistical (id,kg_name,statis_type,statis_rule,create_at,update_at)
SELECT  id,kg_name,type,rule,create_time,update_time
FROM plantdata_manage.t_init_statistical;

-- ----------------------------
-- move to link_share
-- ----------------------------
INSERT INTO link_share (id,user_id,kg_name,spa_id,share_link,is_shared,total_scan,expire_at,create_at,update_at)
SELECT  id,user_id,kg_name,spa_id,share_link,is_share,total_scan,expire_time,create_time,update_time
FROM plantdata_manage.t_user_share;


-- ----------------------------
-- move to menu_favor
-- ----------------------------
INSERT INTO menu_favor (user_id,menu_id,create_at,update_at)
SELECT  user_id,menu_id,create_time,update_time
FROM plantdata_manage.t_user_favor;


-- ----------------------------
-- move to task_graph_reasoning
-- ----------------------------
INSERT INTO task_graph_reasoning (id,kg_name,rule_config,task_id,status,create_at,update_at)
SELECT  rule_id,kg_name,rule_config,task_id,status,create_time,update_time
FROM plantdata_manage.t_reasoning_rule;


-- ----------------------------
-- move to task_graph_search
-- ----------------------------
INSERT INTO task_graph_search (kg_name,task_id,create_at,update_at)
SELECT  kg_name,task_id,create_time,update_time
FROM plantdata_manage.t_user_set;


-- ----------------------------
-- move to task_graph_snapshot
-- ----------------------------
INSERT INTO task_graph_snapshot (id,kg_name,user_id,name,file_size,status,restore_at,create_at,update_at)
SELECT  id,kg_name,user_id,name,file_size,status,restore_time,create_time,update_time
FROM plantdata_manage.t_snapshot;

-- ----------------------------
-- move to system_menu
-- ----------------------------
INSERT INTO system_menu ( id,title, is_enable, is_checked,menu_type, rank,config)
SELECT id, m_name, m_disable, m_checked, m_type, m_order,m_config
FROM plantdata_manage.t_menu;


-- ----------------------------
-- move to user
-- ----------------------------
INSERT INTO user ( id,username, password, status,realname, email,mobile,position,company,industry,create_at,update_at)
SELECT user_id, username, pwd, status, name, email,phone,position,company,profession,create_time,update_time
FROM plantdata_manage.t_user;


-- ----------------------------
-- move to user_apk
-- ----------------------------
INSERT INTO user_apk ( user_id,apk, is_enabled,create_at,update_at)
SELECT user_Id, apk, 1,create_time,update_time
FROM plantdata_manage.t_user_apk;

-- ----------------------------
-- move to user_limit
-- ----------------------------
INSERT INTO user_limit ( user_id,dataset_count, graph_count)
SELECT user_id, data_count, graph_count
FROM plantdata_manage.t_user;

update user_limit a1, plantdata_manage.t_user_share_permission a2 set a1.is_shareable = a2.status
WHERE a1.user_id = a2.user_id;


-- ----------------------------
-- move to user_product
-- ----------------------------
INSERT INTO user_product ( user_id,product_code, status,expire_at,create_at,update_at)
SELECT user_id, system_name, case status WHEN 0 THEN 1 WHEN 1 THEN 2 WHEN 2 THEN 3 ELSE 4 END ,expire_time,create_time,update_time
FROM plantdata_manage.t_user_apply_trial;