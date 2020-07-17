package ai.plantdata.kgcloud.plantdata.converter.semantic;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import ai.plantdata.kgcloud.util.JsonUtils;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 14:32
 */
public class ReasonConverter extends BasicConverter {



    public static RuleBean graphConfReasonRspToRuleBean(@NonNull GraphConfReasonRsp reasonRsp) {
        RuleBean ruleBean = new RuleBean();
        ruleBean.setCreateTime(reasonRsp.getCreateAt());
        ruleBean.setKgName(reasonRsp.getKgName());
        ruleBean.setRuleConfig(JacksonUtils.writeValueAsString(reasonRsp.getRuleConfig()));
        ruleBean.setRuleId(reasonRsp.getId());
        ruleBean.setRuleName(reasonRsp.getRuleName());
        ruleBean.setUpdateTime(reasonRsp.getUpdateAt());
        return ruleBean;
    }

    public static GraphConfReasonReq ruleBeanToGraphConfReasonReq(@NonNull RuleBean ruleBean) {
        GraphConfReasonReq confReasonReq = new GraphConfReasonReq();
        consumerIfNoNull(ruleBean.getRuleConfig(), a -> confReasonReq.setRuleConfig(JsonUtils.stringToMap(a)));
        confReasonReq.setRuleName(ruleBean.getRuleName());
        return confReasonReq;
    }
}
