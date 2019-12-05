package com.plantdata.kgcloud.domain.app.dto;

import com.plantdata.kgcloud.sdk.constant.AggregateEnum;
import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.sdk.constant.CubeTypeEnum;
import com.plantdata.kgcloud.sdk.constant.SecEnum;
import com.plantdata.kgcloud.sdk.constant.SignEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author cjw 2019-11-12 14:19:17
 */
@ApiModel("推理规则参数")
@Getter
@Setter
@Builder
@ToString
public class RelationReasonRuleDTO {
    @ApiModelProperty("属性定义id")
    private Integer attrId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("2 关系 1 属性")
    private AttrDefinitionTypeEnum type;
    @ApiModelProperty("概念id")
    private Long domain;
    @ApiModelProperty("值域")
    private List<Long> rangeList;
    @ApiModelProperty("数据类型")
    private Integer literalDataType;
    private Integer pub;
    private String literalValue;
    private RuleCube nodeRule;
    private List<RuleSection> pathRuleList;
    private Equation compute;

    @Getter
    @Setter
    private static class RuleCube {
        private String id;
        private CubeTypeEnum type;
        private List<FilterBean> filterList;
        private List<FilterBean> aggregateFilterList;
        private Long conceptId;
        private Integer attrId;
        private int direction;
        private int output;
        private LoopBean loop;
        private JSONObject statParam;

    }

    @Getter
    @Setter
    private static class RuleSection {
        private SecEnum sec;
        private int mode; //0 满足 1 不满足
        private List<RuleCube> cubeList;
        private List<Equation> equationList;
    }

    @Getter
    @Setter
    private static class LoopBean {
        private Map<String, Integer> init;
        private Map<Integer, String> loopOperation;
        private HaltBean halt;
        private Map<Integer, String> project;
    }

    @Getter
    @Setter
    private static class HaltBean {
        private Integer maxTimes;
        private Map<Integer, Map<String, Object>> condition;
    }

    @Getter
    @Setter
    private static class Equation {
        private String leftExpression;
        private String rightExpression;
        private SignEnum sign;
    }

    @Getter
    @Setter
    private static class FilterBean {
        private AggregateEnum agg;
        private String attrId;
        private SignEnum sign;
        private Object value;
        private SecEnum sec;
        private String unit;
    }

}
