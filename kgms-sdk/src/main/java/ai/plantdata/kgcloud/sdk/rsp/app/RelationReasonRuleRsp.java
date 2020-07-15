package ai.plantdata.kgcloud.sdk.rsp.app;

import ai.plantdata.kgcloud.sdk.constant.AggregateEnum;
import ai.plantdata.kgcloud.sdk.constant.CubeTypeEnum;
import ai.plantdata.kgcloud.sdk.constant.SecEnum;
import ai.plantdata.kgcloud.sdk.constant.SignEnum;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cjw 2019-11-12 14:19:17
 */
@ApiModel("推理规则参数")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RelationReasonRuleRsp {
    private Integer attrId;
    private String name;
    /**
     *  0 关系 1 属性 2 tag
     */
    private int type;
    /**
     *  0 私有 1 公有
     */
    private int pub;
    private Long domain;
    private List<Long> rangeList = new ArrayList<>();
    private int literalDataType;
    private String literalValue;
    private RuleCube nodeRule;
    private List<RuleSection> pathRuleList;
    private Equation compute;

    @Getter
    @Setter
    @NoArgsConstructor
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
    @NoArgsConstructor
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
    @NoArgsConstructor
    private static class HaltBean {
        private Integer maxTimes;
        private Map<Integer, Map<String, Object>> condition;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Equation {
        private String leftExpression;
        private String rightExpression;
        private SignEnum sign;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class FilterBean {
        private AggregateEnum agg;
        private String attrId;
        private SignEnum sign;
        private Object value;
        private SecEnum sec;
        private String unit;
    }

}
