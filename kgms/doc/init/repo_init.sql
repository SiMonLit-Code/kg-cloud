use `kg_cloud_kgms`

DROP TABLE IF EXISTS `repo_use_log`;
CREATE TABLE `repo_use_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `click_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  `type` tinyint(1) UNSIGNED NOT NULL COMMENT '菜单id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点击使用表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `repo_menu`;
CREATE TABLE `repo_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `repository_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100931 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件菜单关联表' ROW_FORMAT = Dynamic;

INSERT INTO `repo_menu` VALUES (6, 902002);
INSERT INTO `repo_menu` VALUES (95, 1001001);
INSERT INTO `repo_menu` VALUES (146, 902002);
INSERT INTO `repo_menu` VALUES (191, 1001001);
INSERT INTO `repo_menu` VALUES (193, 1001001);
INSERT INTO `repo_menu` VALUES (200, 1001001);
INSERT INTO `repo_menu` VALUES (201, 1001001);
INSERT INTO `repo_menu` VALUES (202, 1001004);
INSERT INTO `repo_menu` VALUES (400, 902002);
INSERT INTO `repo_menu` VALUES (401, 902003);
INSERT INTO `repo_menu` VALUES (924, 904001);
INSERT INTO `repo_menu` VALUES (927, 904002);
INSERT INTO `repo_menu` VALUES (934, 904001);
INSERT INTO `repo_menu` VALUES (935, 904001);
INSERT INTO `repo_menu` VALUES (952, 1001001);
INSERT INTO `repo_menu` VALUES (953, 1001001);
INSERT INTO `repo_menu` VALUES (1020, 904002);
INSERT INTO `repo_menu` VALUES (1230, 1001001);
INSERT INTO `repo_menu` VALUES (1310, 1001001);
INSERT INTO `repo_menu` VALUES (4100, 902002);
INSERT INTO `repo_menu` VALUES (4101, 902003);
INSERT INTO `repo_menu` VALUES (100900, 1001001);
INSERT INTO `repo_menu` VALUES (100930, 1001001);


DROP TABLE IF EXISTS `repo_item_group`;
CREATE TABLE `repo_item_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
  `rank` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1005 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件分组' ROW_FORMAT = Dynamic;

INSERT INTO `repo_item_group` VALUES (1, 0, '自助式应用扩展包', 1, '提供Web、桌面、office助手、文档加工等自助式应用');
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

INSERT INTO `repo_item` VALUES (1001, 'Web助手', 'DATA', 1, 1, 1, '以Chrome插件的方式提供轻量级知识加工、一键收集，并快速将文本、文档、网页链接与图谱实体进行关联，同时在阅读相关文献时提供辅助查询能力。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgms\"}]');
INSERT INTO `repo_item` VALUES (1002, '桌面助手', 'DATA', 1, 1, 2, '以桌面端方式提供轻量级文档助手，支持知识收藏与订阅，支持知识的查询与问答。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgms\"}]');
INSERT INTO `repo_item` VALUES (1003, 'office助手', 'DATA', 1, 1, 3, '基于构建完成的图谱数据，以Office插件的方式提供辅助文档编制能力，支持文档模板的维护及引入，支持文档检索与引用，支持图谱知识问答及引入。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgms\"}]');
INSERT INTO `repo_item` VALUES (1004, 'KGDP', 'SUB_SYSTEM', 1, 1, 4, '复杂文本加工组件，提供外部文档资源加工成结构化数据并入图的标注平台，支持篇、章、段拆解，支持半自动化实体、关系、属性等要素标注，支持加工结果入图。', '{\"url\":[{\"string\":\"/kgdp\"}]}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgdp\"}]');
INSERT INTO `repo_item` VALUES (1005, 'AutoDI', 'DATA', 1, 1, 5, '企业级数据智能编排组件，提供拖拽配置、自然语言交互等方式，快速编排数据可视化大屏、智能分析面板、搜索系统等数据智能应用。', '{}', '[]');
INSERT INTO `repo_item` VALUES (2001, 'KGSearch', 'DATA', 2, 1, 1, '文本搜索引擎，提供基础的搜索能力，搜索能力可扩展。', '{}', '[]');
INSERT INTO `repo_item` VALUES (2002, 'NLP增强搜索软件包', 'DATA', 2, 1, 1, '通过NLP能力增强搜索，支持接入意图识别、实体链接、语义扩展等NLP模型，优化搜索体验。', '{}', '[]');
INSERT INTO `repo_item` VALUES (2003, '图谱增强搜索软件包', 'DATA', 2, 1, 1, '基于知识图谱提升搜索的准确率，丰富搜索结果展示，支持同义扩展、语义搜索等。', '{}', '[]');
INSERT INTO `repo_item` VALUES (2004, '搜索用户日志计算软件包', 'DATA', 2, 1, 1, '面向用户搜索日志，提供多种日志计算与分析模型，深度挖掘用户搜索行为。', '{}', '[]');
INSERT INTO `repo_item` VALUES (2005, '运营指标计算软件包', 'DATA', 2, 1, 1, '基于用户搜索日志，提供用户点击率、MAP、NDCG等指标计算能力。', '{}', '[]');
INSERT INTO `repo_item` VALUES (3001, 'KGBot', 'SUB_SYSTEM', 3, 1, 1, '知识图谱问答组件，基于已构建的图谱，通过自然语言交互完成知识查询、知识可视化、知识统计、挖掘等一系列服务，提供问答服务组件，支持针对实体、属性、关系、边属性等知识类型的问答。', '{\"url\":[{\"string\":\"/kgbot\"}]}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgbot\"}]');
INSERT INTO `repo_item` VALUES (3002, '扩展问答配置软件包', 'DATA', 3, 1, 1, '在基本知识图谱问答能力的基础上，提供语义模型配置，语义模板配置，词库配置等高级配置管理功能，实现精准意图理解。', '{}', '[]');
INSERT INTO `repo_item` VALUES (3003, '多策略问答软件包', 'DATA', 3, 1, 1, '在知识图谱问答的基础上，引入FAQ、MRC、TaskQA等问答实现方法，形成统一多策略问答模型，满足不同数据场景的需要，并提供对应的多策略意图路由配置。', '{}', '[]');
INSERT INTO `repo_item` VALUES (3004, '日志计算软件包', 'DATA', 3, 1, 1, '面向用户的问答日志，提供多种日志计算模型，迭代优化问答在线过程中各类模型参数和知识质量。', '{}', '[]');
INSERT INTO `repo_item` VALUES (4001, '基础推荐软件包', 'DATA', 4, 1, 1, '搜索推荐引擎，提供基础的推荐能力，推荐能力可扩展。', '{}', '[]');
INSERT INTO `repo_item` VALUES (4002, 'NLP增强型搜索软件包', 'DATA', 4, 1, 1, '通过NLP模型增强推荐能力，支持接入意图识别、实体链接、语义扩展等NLP模型，优化推荐结果。', '{}', '[]');
INSERT INTO `repo_item` VALUES (4003, '图谱增强型搜索软件包', 'DATA', 4, 1, 1, '通过知识图谱增强推荐能力，提升推荐的准确率，丰富推荐结果展示。', '{}', '[]');
INSERT INTO `repo_item` VALUES (4004, '推荐用户日志优化计算软件包', 'DATA', 4, 1, 1, '面向用户操作日志，提供多种日志计算与分析模型，迭代优化知识推荐的质量。', '{}', '[]');
INSERT INTO `repo_item` VALUES (4005, '运营指标计算软件包', 'DATA', 4, 1, 1, '基于用户操作日志，提供用户点击率、MAP、NDCG等指标计算能力。', '{}', '[]');
INSERT INTO `repo_item` VALUES (11001, 'NLP Lab', 'SUB_SYSTEM', 11, 1, 1, 'NLP模型库，提供分词、命名实体识别、属性抽取、关系抽取、实体链接、关键词抽取、文本摘要生成等预构建模型。', '{\"url\":[{\"string\":\"/kgtext\"}]}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgtext\"}]');
INSERT INTO `repo_item` VALUES (11002, '协同标注软件包', 'DATA', 11, 1, 1, '扩展语料标注能力，支持多人协同标注，主账号分配任务，多个子账号协同标注；用户可自定义标注标签，提供远程弱监督的方式进行语料标注。', '{}', '[]');
INSERT INTO `repo_item` VALUES (11003, '在线训练软件包', 'DATA', 11, 1, 1, '在线模型训练组件，支持实体识别、属性抽取、关系抽取、同义抽取、事件抽取等深度学习模型训练，提供训练过程和结果的可视化统计、训练日志查看。', '{}', '[]');
INSERT INTO `repo_item` VALUES (11004, '文本生产软件包', 'DATA', 11, 1, 1, '文本抽取组件，提供实体识别、属性抽取、关系抽取、同义抽取、事件抽取，支持抽取结果统计、查看、修改，支持用户自定义规则对抽取结果进行核查。', '{}', '[]');
INSERT INTO `repo_item` VALUES (501001, '论文模式', 'DATA', 501, 1, 1, '来自知网、万方等知识服务平台的模式抽象，涵盖作者、关键词、机构、期刊、论文等概念，以及英文标题、简介等属性。', '{}', '[]');
INSERT INTO `repo_item` VALUES (502001, '企业图谱模式', 'DATA', 502, 1, 1, '来自启信宝、天眼查等企业信息查询的模式抽象，涵盖公司、欠税信息等概念，以及公司名称、负责人姓名等属性。', '{}', '[]');
INSERT INTO `repo_item` VALUES (503001, '纪检领域人物关联图谱模式', 'DATA', 503, 1, 1, '来自纪检领域的模式抽象，涵盖了职业、地点、房产、机构等概念，以及产权证号、户籍类型等属性。', '{}', '[]');
INSERT INTO `repo_item` VALUES (601001, '百科知识图谱', 'DATA', 601, 1, 1, '数据来源于百度百科、互动百科、维基百科（中文部分），涵盖了通用领域知识，实体数据规模达到达千万级别。', '{}', '[]');
INSERT INTO `repo_item` VALUES (601002, '世界地理知识图谱', 'DATA', 601, 1, 1, '数据来源于knoema-世界数据图册，涵盖了世界和地区统计数据、地图、排名，包含行政区划相关的约1万个实体。', '{}', '[]');
INSERT INTO `repo_item` VALUES (601003, '自然人文地理知识图谱', 'DATA', 601, 1, 1, '数据来源于OpenKG，涵盖了自然地理知识，包含约50万个实体。', '{}', '[]');
INSERT INTO `repo_item` VALUES (601004, '世界知识图谱', 'DATA', 601, 1, 1, '数据来源于世界知识年鉴，涵盖了世界政治经济大事和各国家、地区的基本情况，包含约1万个实体。', '{}', '[]');
INSERT INTO `repo_item` VALUES (602001, '目标知识图谱', 'DATA', 602, 1, 1, '数据来源于环球网-万国武器，涵盖了各国的武器装备知识，包含约1万个实体。', '{}', '[]');
INSERT INTO `repo_item` VALUES (701001, '事件拐点', 'DATA', 701, 1, 1, '面向军政领域提供事件拐点业务模型。', '{}', '[]');
INSERT INTO `repo_item` VALUES (702001, '企业谱系', 'DATA', 702, 1, 1, '面向金融领域提供企业谱系分析业务模型。', '{}', '[]');
INSERT INTO `repo_item` VALUES (702002, '实际控制人', 'DATA', 702, 1, 1, '面向金融领域提供实际控制人分析业务模型。', '{}', '[]');
INSERT INTO `repo_item` VALUES (801001, '人名、地名、机构名实体识别模型', 'DATA', 801, 1, 1, '面向通用领域，提供人名、地名、机构名等命名实体的识别，综合识别准确率在90%以上。', '{}', '[]');
INSERT INTO `repo_item` VALUES (802001, '实体识别模型', 'DATA', 802, 1, 1, '面向军政领域，提供军事人物、军事目标、军事武器装备等领域内命名实体的识别，综合识别准确率在85%以上。', '{}', '[]');
INSERT INTO `repo_item` VALUES (802002, '属性抽取模型', 'DATA', 802, 1, 1, '面向军政领域，提供军事装备的型号、生产日期、首飞时间等实体属性的抽取，综合识别准确率在85%以上。', '{}', '[]');
INSERT INTO `repo_item` VALUES (802003, '关系抽取模型', 'DATA', 802, 1, 1, '面向军政领域，提供军事雇佣、任职、装备所属国家等实体关系的抽取，综合识别准确率达85%。', '{}', '[]');
INSERT INTO `repo_item` VALUES (803001, '实体识别模型', 'DATA', 803, 1, 1, '面向金融财经领域，提供金融公司、人物、金融产品等命名实体的识别，综合识别准确率在85%以上。', '{}', '[]');
INSERT INTO `repo_item` VALUES (803002, '属性抽取模型', 'DATA', 803, 1, 1, '面向金融财经领域，提供上市公司的收购时间、成立时间、地址等实体属性的识别，综合识别准确率在80%以上。', '{}', '[]');
INSERT INTO `repo_item` VALUES (803003, '关系抽取模型', 'DATA', 803, 1, 1, '面向金融财经领域，提供买卖、合作、竞争、任职等实体关系的抽取，综合识别准确率达80%。', '{}', '[]');
INSERT INTO `repo_item` VALUES (901001, 'KGMS集群版', 'DATA', 901, 1, 1, '升级KGMS底层组件，提升产品性能，支撑大规模知识图谱的全生命周期管理。', '{}', '[]');
INSERT INTO `repo_item` VALUES (901002, 'KGMS内存高性能版', 'DATA', 901, 1, 1, '使用原生图结构的并行分布式图数据库PlantGraph，支持单机环境百亿级、分布式环境万亿级三元组图数据的高效存储与计算。', '{}', '[]');
INSERT INTO `repo_item` VALUES (902001, '模式自动规约软件包', 'DATA', 902, 1, 1, '集成自动规约计算模型，支持概念规约、属性规约的自动推荐，提供自底向上的知识模式自动构建方法，扩展知识建模能力。', '{}', '[{\"checkType\":\"MONGO\",\"content\":\"default_graph\"}]');
INSERT INTO `repo_item` VALUES (902002, '图谱自动融合软件包', 'DATA', 902, 1, 1, '集成基于规则的融合计算模型，提供词典、属性、标签、实体关联关系4类规则，扩展知识计算能力，提升知识质量。', '{}', '[{\"checkType\":\"FILE\",\"content\":\"/work/azkaban_shell/merge.sh\"}]');
INSERT INTO `repo_item` VALUES (902003, '知识自动标引软件包', 'DATA', 902, 1, 1, '集成知识链接相关算法模型，支持文件、文本、链接类数据的知识标引，扩展知识图谱的知识范围。', '{}', '[]');
INSERT INTO `repo_item` VALUES (902004, '规则推理计算软件包', 'DATA', 902, 1, 1, '集成基于规则的推理模型，支持可视化配置推理参数、实时跟踪推理任务执行情况、查看推理结果并选择性入图。', '{}', '[]');
INSERT INTO `repo_item` VALUES (902005, '神经网络推理计算软件包', 'DATA', 902, 1, 1, '集成基于神经网络的推理模型，支持可视化配置神经网络任务、实时跟踪推理任务执行情况、查看推理结果并选择性入图。', '{}', '[]');
INSERT INTO `repo_item` VALUES (902006, '离线数据接入软件包', 'DATA', 902, 1, 1, '提供D2R任务可视化配置、任务调度管理、任务执行情况查看、冲突数据监测能力，实现源数据离线入图。', '{}', '[]');
INSERT INTO `repo_item` VALUES (902007, '实时数据接入软件包', 'DATA', 902, 1, 1, '提供订阅式数仓数据接入，支持数据自动读取、实时接入。', '{}', '[]');
INSERT INTO `repo_item` VALUES (904001, '时序可视化分析软件包', 'DATA', 904, 1, 1, '提供面面向知识图谱的时序知识可视化与分析的知识应用，包括时序图探索、时序路径发现、时序关联关系。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgms\"}]');
INSERT INTO `repo_item` VALUES (904002, '空间可视化分析软件包', 'DATA', 904, 1, 1, '提供面面向知识图谱的空间知识可视化与分析的知识应用，包括GIS分析、GIS轨迹分析。', '{}', '[{\"checkType\":\"CONSUL\",\"content\":\"kgms\"}]');
INSERT INTO `repo_item` VALUES (1001001, 'KGDW', 'DATA', 1001, 1, 1, '针对行业标准数据、系统标准数据以及自定义数据三种数据进行半自动数据建模、发布模式、D2R订阅的实时数据流构图流程。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1002001, 'MySQL', 'DATA', 1002, 1, 1, 'MySQL数据接入驱动，支持MySQL版本为5.1~5.7。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1002002, 'MongoDB', 'DATA', 1002, 1, 1, 'MongoDB数据接入驱动，支持MongoDB版本为3.6.10。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1002003, 'Oracle', 'DATA', 1002, 1, 1, 'Oracle数据接入驱动，支持Oracle版本为11.2。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1003001, '启信宝', 'DATA', 1003, 1, 1, '对接启信宝，下载数据格式构建企业关联关系图谱。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1003002, '论文', 'DATA', 1003, 1, 1, '对接知网，下载数据格式构建论文知识图谱。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1003003, '专利', 'DATA', 1003, 1, 1, '对接国家知识产权局专利局，下载数据格式构建专利知识图谱。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1004001, '数仓基础统计API', 'DATA', 1004, 1, 1, '针对数据仓库，提供基础统计API，包括二维表和三维表统计。', '{}', '[]');
INSERT INTO `repo_item` VALUES (1004002, '数仓DAX增强统计API', 'DATA', 1004, 1, 1, '针对数据仓库，提供DAX增强统计API，支持跨表、跨数据库、跨服务器的数仓数据统计。', '{}', '[]');


DROP TABLE IF EXISTS `repo_base_menu`;
CREATE TABLE `repo_base_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `repository_id` int(11) UNSIGNED NOT NULL COMMENT '组件id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10094 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组件基础菜单表' ROW_FORMAT = Dynamic;

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
INSERT INTO `repo_base_menu` VALUES (54, 0);
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
