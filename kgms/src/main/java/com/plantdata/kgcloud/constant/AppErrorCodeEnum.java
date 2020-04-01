package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 9:52
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AppErrorCodeEnum implements ErrorCode {
    /**
     *
     */
    /**
     * app模块
     */
    CONF_ALGORITHM_NOT_EXISTS(120601, "算法配置不存在"),
    CONF_KGQL_NOT_EXISTS(120602, "图谱业务不存在"),
    CONF_QA_NOT_EXISTS(120603, "图谱问答不存在"),
    CONF_REASONING_NOT_EXISTS(120604, "图谱推理不存在"),
    ES_CONFIG_NOT_FOUND(120605, "未找到es配置"),
    GRAPH_TYPE_ERROR(120606, "图谱类型错误"),
    MY_DATA_NULL_ES(120607, "数据集不为搜索数据集"),
    DATE_PARSE_ERROR(120608, "数据时间格式转换失败"),
    EDGE_ATTR_DEF_NULL(120609, "边属性定义不存在"),
    CONF_STATISTICALID_NOT_EXISTS(120610, "图谱统计ID不存在"),
    CONF_STATISTICAL_NOT_EXISTS(120611, "图谱统计不存在"),
    CONF_IDORIDS_NOT_EXISTS(120612, "您输入的id不存在"),
    GIS_TYPE_ERROR(120618, "gis筛选类型错误"),
    GIS_INFO_ERROR(120619, "gis筛选坐标格式错误"),

    NULL_GIS_RULE_ID(120610, "规则id不能为空"),
    NULL_GIS_ENTITY_ID(120611, "实体ids不能为空"),
    NULL_GIS_KG_QL(120611, "kgQl语句不能为空"),
    NULL_CONCEPT_ID_AND_KEY(120611, "概念id和key不能同时为空"),
    EXPORT_TYPE_ERROR(120612, "导出类型错误"),
    ALGORITHM_PARAM_ERROR(120613, "算法参数错误"),
    NULL_KW_AND_ID(120614, "实体名称和id不能同时为空"),
    ERROR_DATA_SET_QUERY(120615, "数据集查询或排序语法错误"),
    IMAGE_NO_INCLUDE_DATA_ERROR(120616, "data参数不含data："),
    GRAMMAR_ERROR(120617, "语法错误"),
    PARAM_ANALYSIS_ERROR(120618, "参数解析错误"),
    DATA_FORMAT_ERROR(120620, "时间格式错误"),
    ALGORITHM_EXECUTE_ERROR(120619, "算法执行失败"),
    ES_RULE_ERROR(120620, "es语法错误"),
    ATTR_DEF_ANY_NO_NULL(120621, "属性id和key不能同时为空"),
    ATTR_DEF_NOT_FOUNT(120622, "属性定义不存在"),
    REASON_RULE_ERROR(120623, "推理规则错误");
    private final int errorCode;

    private final String message;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
