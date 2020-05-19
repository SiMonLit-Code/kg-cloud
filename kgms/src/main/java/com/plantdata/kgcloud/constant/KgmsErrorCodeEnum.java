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
    QUERYSETTING_NOT_EXISTS(120101, "规则配置不存在"),
    TASK_STATUS_NOT_EXISTS(120102, "任务状态记录不存在"),
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

    ILLEGAL_PARAM(120512, "参数不合法"),

    PERMISSION_NOT_MODEL_UPLOAD_ERROR(120601, "没有模式上传权限"),

    MODEL_PARSER_ERROR(120602, "模式解析错误"),

    TAG_JSON_PASER_ERROR(120603, "数据库打标解析错误"),

    EMTRY_MODEL_PUDH_ERROR(120604, "空模式不能发布"),

    SCHEMA_CONCEPT_NOT_EXIST_ERROR(120605,"图谱没有定义概念"),

    MODEL_FILE_TITLE_ERROR(120606,"模式文件表头错误，请根据模板上传"),

    EXCEL_TYPE_ERROR(120607,"excel文件格式错误"),

    FILE_SIZE_OVER(120608,"文件关联大小不能超过20M"),

    RELATION_IS_EXIST(120609,"文件关联已存在"),

    EXCEL_DATA_ERROR(120610,"表头内容错误"),

    EXCEL_DATA_NULL(120611,"excel数据为空"),
            ;
    private final int errorCode;

    private final String message;

    KgmsErrorCodeEnum(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
