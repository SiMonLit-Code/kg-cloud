package ai.plantdata.kgcloud.plantdata.converter.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import ai.plantdata.kgcloud.plantdata.bean.rule.GraphRuleBean;
import ai.plantdata.kgcloud.plantdata.bean.rule.GraphmRuleMapBean;
import ai.plantdata.kgcloud.plantdata.constant.StringConstants;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.req.rule.GraphRuleAdd;
import ai.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import ai.plantdata.kgcloud.util.JsonUtils;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 10:13
 */
public class GraphRuleConverter extends BasicConverter {


    public static GraphConfKgqlReq graphRuleMapBeanToGraphConfKgqlReq(GraphmRuleMapBean ruleMapBean) {
        GraphConfKgqlReq confKgqlReq = new GraphConfKgqlReq();
        confKgqlReq.setKgql(ruleMapBean.getRuleKgql());
        confKgqlReq.setKgqlName(ruleMapBean.getRuleName());
        confKgqlReq.setRuleType(ruleMapBean.getRuleType());
        return confKgqlReq;
    }

    public static GraphConfKgqlReq graphRuleAddToGraphConfKgqlReq(GraphRuleAdd ruleAdd) {
        return graphRuleMapBeanToGraphConfKgqlReq(ruleAdd.getBean());
    }


    public static GraphmRuleMapBean graphConfKgQlRspToGraphRuleMapBean(GraphConfKgqlRsp rsp) {
        GraphmRuleMapBean ruleMapBean = new GraphmRuleMapBean();
        ruleMapBean.setCreateTime(rsp.getCreateAt());
        ruleMapBean.setKgName(rsp.getKgName());
        consumerIfNoNull(rsp.getId(), ruleMapBean::setRuleId);
        ruleMapBean.setRuleName(rsp.getKgqlName());
        ruleMapBean.setRuleKgql(rsp.getKgql());
        ruleMapBean.setRuleType(rsp.getRuleType());
        ruleMapBean.setUpdateTime(rsp.getUpdateAt());
        consumerIfNoNull(rsp.getRuleSettings(), a -> {
            Map<String, Object> objectMap = JsonUtils.jsonToObj(a, new TypeReference<Map<String, Object>>() {
            });
            GraphRuleBean bean = new GraphRuleBean();
            consumerIfNoNull(objectMap.get(StringConstants.DOMAIN), b -> applyIfTrue(objectMap.containsKey(StringConstants.DOMAIN), Long.valueOf(b.toString()), bean::setDomain));
            ruleMapBean.setRuleSettings(bean);
        });
        return ruleMapBean;
    }
}
