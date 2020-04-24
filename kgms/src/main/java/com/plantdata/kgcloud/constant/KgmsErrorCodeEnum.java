package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 18:22
 **/
@Getter
public enum KgmsErrorCodeEnum implements ErrorCode {
    /**
     * errorCode 统一为6位数字
     * 前 两 位: 服务标识，默认以服务端口后两位 固定为12
     * 中间两位: 模块标识，
     * 后 两 位: 业务错误标识
     */
    DATASET_NOT_EXISTS(120101, "数据集不存在"),
    FOLDER_NOT_EXISTS(120102, "文件夹不存在"),
    FOLDER_DISABLE_DELETE(120103, "默认文件夹不允许删除"),
    DATASET_TYPE_NONSUPPORT(120104, "不支持的数据集类型"),
    DATASET_CONNECT_ERROR(120105, "数据集连接失败"),
    DATASET_KEY_EXISTS(120106, "唯一标识已存在"),
    DATASET_EXPORT_FAIL(120107, "数据集导出失败"),
    DATASET_IMPORT_FAIL(120108, "数据集导入失败"),
    DATASET_FIELD_ERROR(120109, "数据字段类型校验错误"),
    QUERYSETTING_NOT_EXISTS(120110, "规则配置不存在"),

    KTR_SAVE_FAIL(120113, "kettle文件生成失败"),

    ANNOTATION_NOT_EXISTS(120109, "标引不存在"),
    DATASET_ES_REQUEST_ERROR(120110, "es请求失败"),
    DATASET_ES_KEY_EXISTS(120111, "es唯一标识已存在"),
    TASK_STATUS_NOT_EXISTS(120112, "任务状态记录不存在"),
    DATASET_CONNECT_PDTEXT_ERROR(120113, "连接文本数据集失败"),
    MODEL_NOT_EXISTS(120201, "模型不存在"),
    /**
     * app模块
     */
    CONF_ALGORITHM_NOT_EXISTS(120601, "算法配置不存在"),
    CONF_KGQL_NOT_EXISTS(120602, "图谱业务不存在"),
    CONF_KGQLQUERYSETTING_ERROR(120614, "图谱业务规则错误"),
    CONF_QA_NOT_EXISTS(120603, "图谱问答不存在"),
    CONF_REASONING_NOT_EXISTS(120604, "图谱推理不存在"),
    CONF_STATISTICAL_NOT_EXISTS(120606, "图谱统计不存在"),
    ES_CONFIG_NOT_FOUND(120605, "未找到es配置"),
    GRAPH_TYPE_ERROR(120606, "图谱类型错误"),
    MY_DATA_NULL_ES(120607, "数据集不为搜索数据集"),
    DATE_PARSE_ERROR(120608, "数据时间格式转换失败"),
    EDGE_ATTR_DEF_NULL(120609, "边属性定义不存在"),
    TAG_HAVE_EXISTED(120610, "实体标签已存在"),
    ENTITY_TEMPLATE_NEED_CONCEPT_ID(120611, "实体模板下载需要概念id"),
    SPECIFIC_TEMPLATE_NEED_ATTR_ID(120612, "特定关系模板下载需要属性id"),
    YOURSELF_NOT_AS_PARENT(120613, "自身不能作为父概念"),
    SYNC_TASK_ONLY_ONE(120614, "一个图谱只能有一个异步任务"),
    SAME_ATTRIBUTE_ONLY_EXIST_ONE(120615, "同一个属性只能在属性分组里面存在一次"),
    TIME_FORM_MORE_THAN_TO(120616, "开始时间不能大于截止时间"),
    FILE_TYPE_ERROR(120617, "文件导入类型不正确"),
    FILE_OUT_LIMIT(120619, "文件大小超出限制"),
    METADATA_TYPE_ERROR(120618, "metadata数据类型不正确"),
    DICTIONARY_NOT_EXISTS(120301, "词典不存在"),
    DICTIONARY_IMPORT_FAIL(120302, "词典导入失败"),
    CONF_FOCUS_ENTITIES_SIZE_ERROR(120620, "您至少设置两个节点"),
    CONF_IDORIDS_NOT_EXISTS(120621, "您输入的id不存在"),
    CONF_STATISTICALID_NOT_EXISTS(120622, "图谱统计ID不存在"),
    CONF_FOCUS_ERROR(120623, "请您选择正确节点"),
    PRIVATE_RELATION_HAS_EXIST(120624, "私有关系已存在"),
    DOMAIN_WORD_NOT_EMPTY(120625, "领域词不能为空"),
    DOMAIN_CONCEPT_NOT_EMPTY(120626, "领域词相关概念不能为空"),
    CONF_QUERYSETTING_ERROR(120627, "图谱关系配置错误"),
    CONF_RULENAME_ERROR(120628, "图谱关系名称要在64位以内"),


    WORD_NOT_EXISTS(120302, "词条不存在"),

    GRAPH_NOT_EXISTS(120401, "图谱不存在"),

    GRAPH_CREATE_FAIL(120402, "图谱创建失败"),

    GRAPH_OUT_LIMIT(120403, "超过图谱创建限制,请联系管理员"),

    REMOTE_SERVICE_ERROR(120501, "远程服务错误"),

    BASIC_INFO_NOT_EXISTS(120502, "概念或实体不存在"),

    ATTRIBUTE_DEFINITION_NOT_EXISTS(120503, "属性定义不存在"),

    DATA_CONVERSION_ERROR(120504, "数据转换错误"),

    FILE_IMPORT_ERROR(120505, "文件导入错误"),

    ATTR_GROUP_NOT_EXISTS(120506, "属性分组不存在"),

    ATTR_GROUP_NOT_EXISTS_SAME_NAME(120507, "属性分组不能存在同名分组"),

    ATTR_TEMPLATE_ERROR(120507, "属性模板配置不正确"),

    ATTR_TEMPLATE_NOT_EXISTS(120508, "属性模板不存在"),

    PERMISSION_NOT_ENOUGH_ERROR(120509, "没有该图谱的编辑权限"),

    RDF_EXPORT_ERROR(120510, "rdf导出错误"),

    GRAPH_QUALITY_NOT_EXIST(120511, "图谱质量不存在"),

    ILLEGAL_PARAM(120512, "参数不合法"),

    STANDARD_TEMPLATE_NOT_EXIST(120601, "行业标准模板不存在"),

    PRE_BUILD_MODEL_NOT_EXIST(120602, "预构建模式不存在"),

    PERMISSION_NOT_MODEL_UPLOAD_ERROR(120603, "没有模式上传权限"),

    MODEL_PARSER_ERROR(120604, "模式解析错误"),

    CYCLE_CONCEPT_EXIST(120605, "模式存在循环概念"),

    DW_DATABASE_NOT_EXIST(120701, "数仓数据库不存在"),

    DW_TABLE_CREATE_ERROR(120702, "数仓数据表生成失败"),

    DW_TABLE_NOT_EXIST(120703, "数仓数据表不存在"),

    YAML_PARSE_ERROR(120704, "yaml文件格式错误"),

    REMOTE_TABLE_FIND_ERROR(120705, "获取远程表错误"),

    TABLE_CREATE_WAY_ERROR(120706, "表创建方式不适用该功能"),

    DATABASE_DATAFORMAT_ERROR(120707, "数据库创建类型不适用该功能"),

    TAG_JSON_PASER_ERROR(120708, "数据库TAG_JSON解析错误"),

    FILE_SCHEMAPASER_ERROR(120709, "文件schema解析错误"),

    EMTRY_MODEL_PUDH_ERROR(120710, "空模式不能发布"),

    MAP_TABLE_EXIST(120711, "映射的行业表已存在"),

    REMOTE_TABLE_EXIST(120712, "该远程表已存在"),

    TABLE_NAME_EXIST(120713, "该表名已存在"),

    TABLE_CONNECT_ERROR(120714, "表连接失败"),

    EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR(120715, "tableName不存在，请先创建再上传模式"),

    EXAMPLE_FILE_DOWNLOAD_ERROR(120716, "示例文件下载失败"),

    SCHEMA_PARENT_CONCEPT_NOT_EXIST_ERROR(120717, "模式父概念不存在"),

    SCHEMA_CONCEPT_CIRCULAR_REFERENCE_ERROR(120718, "模式存在循环引用"),

    SCHEMA_PASER_LACK_REQUIRES_ERROR(120719, "模式解析缺少必填项"),

    SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR(120720, "模式解析定义域不存在"),

    SCHEMA_PASER_RANGE_NOT_EXIST_ERROR(120721, "模式解析值域不存在"),

    SCHEMA_PASER_DATATYPE_NOT_EXIST_ERROR(120722, "模式解析数据类型不存在"),

    SCHEMA_PASER_CONCEPT_EXIST_ERROR(120723, "模式解析存在同名概念"),

    TABLE_SCHEMA_MISMATCHING_STIPULATE(120724, "结构不符合行业标准模板要求"),

    TABLE_NOT_EXIST_IN_DATABASE(120725, "打标文件中存在未添加表"),

    YAML_FILE_EMTRY_ERROR(120726, "yaml文件为空，请检查后再上传"),

    YAML_TABLES_NOT_EXIST_ERROR(120727, "yaml缺少tables字段"),

    YAML_TABLES_IS_EMTRY_ERROR(120728, "yaml文件tables信息为空"),

    YAML_ATTR_DOMAIN_NOT_EXIST_ERROR(120729, "定义域不存在"),

    YAML_ATTR_RANGE_NOT_EXIST_ERROR(120730, "值域不存在"),

    YAML_COLUMN_IS_EMTRY_ERROR(120731, "colums为空"),

    YAML_COLUMN_TAG_PARSER_ERROR(120732, "colums字段中tag格式不正确"),

    YAML_COLUMN_TAG_NOT_EXIST(120733, "colums字段中tag不存在或数据为空"),

    YAML_COLUMN_TYPE_NOT_EXIST(120734, "colums字段中type不存在或数据为空"),

    YAML_COLUMN_TYPE_PARSER_ERROR(120735, "colums字段中type枚举错误"),

    YAML_COLUMN_NOT_EXIST_CONCEPT(120736, "colums字段没有定义概念"),

    YAML_COLUMN_NOT_EXIST_CONCEPT_NAME(120737, "colums字段没有定义概念名称信息"),

    YAML_RELATION_PARSER_ERROR(120738, "yaml文件relation字段格式不正确"),

    YAML_TABLES_CONFIG_IS_EMTRY_ERROR(120739, "yaml文件tables信息未配置"),

    TAG_ATTR_TYPE_PARSER_ERROR(120740, "属性dataType枚举错误"),

    YAML_COLUMS_NOT_EXIST_IN_TABLE(120741, "colums字段在表中不存在"),

    DATA_STORE_STATISTIC_TYPE_ERROR(120742, "统计类型错误"),

    DW_OUT_LIMIT(120743, "超过数仓创建限制,请联系管理员"),

    SCHEMA_CONCEPT_NOT_EXIST_ERROR(120744,"图谱没有定义概念"),

    UNZIP_ERROR(120745,"解压失败"),

    LABEL_ENUM_NOT_EMTRY(120746,"枚举值不能为空"),

    NOT_SELECT_MAP_TABLE(120747,"未选择映射表"),
    MODEL_FILE_TITLE_ERROR(120748,"模式文件表头错误，请根据模板上传"),
    NO_DATA_CHANGE(120749,"数据没有改动"),
            ;
    private final int errorCode;

    private final String message;

    KgmsErrorCodeEnum(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
