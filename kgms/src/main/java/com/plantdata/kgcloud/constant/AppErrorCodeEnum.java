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
    CONF_REASONING_NOT_EXISTS(120604, "图谱统计不存在"),
    CONF_STATISTICAL_NOT_EXISTS(120606, "图谱统计不存在"),
    ES_CONFIG_NOT_FOUND(120605, "未找到es配置"),
    GRAPH_TYPE_ERROR(120606, "图谱类型错误"),
    MY_DATA_NULL_ES(120607, "数据集不为搜索数据集"),
    DATE_PARSE_ERROR(120608, "数据时间格式转换失败"),
    EDGE_ATTR_DEF_NULL(120609, "边属性定义不存在"),

    NULL_GIS_RULE_ID(120610, "规则id不能为空"),
    NULL_GIS_ENTITY_ID(120611, "实体ids不能为空"),
    NULL_GIS_KG_QL(120611, "kgQl语句不能为空"),
    NULL_CONCEPT_ID_AND_KEY(120611, "概念id和key不能同时为空"),
    EXPORT_TYPE_ERROR(120612, "导出类型错误"),
    ALGORITHM_PARAM_ERROR(120613, "算法参数错误"),
    ;
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
