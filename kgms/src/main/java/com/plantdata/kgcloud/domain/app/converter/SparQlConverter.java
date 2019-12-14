package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.ql.resp.NodeBean;
import ai.plantdata.kg.api.ql.resp.QueryResultVO;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.QueryResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.sparql.SparQlNodeRsp;
/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 9:33
 */
public class SparQlConverter extends BasicConverter {

    public static QueryResultRsp queryResultVoToRsp(QueryResultVO result) {
        QueryResultRsp queryResultRsp = new QueryResultRsp();
        queryResultRsp.setCount(result.getCount());
        queryResultRsp.setResult(listToRsp(result.getResult(), b -> listToRsp(b, SparQlConverter::nodeBeanToSparQlNodeRsp)));
        return queryResultRsp;
    }

    private static SparQlNodeRsp nodeBeanToSparQlNodeRsp(NodeBean nodeBean) {
        SparQlNodeRsp sparQlNodeRsp = new SparQlNodeRsp();
        sparQlNodeRsp.setId(nodeBean.getId());
        sparQlNodeRsp.setKey(nodeBean.getKey());
        sparQlNodeRsp.setType(nodeBean.getType());
        ///todo 问丁浩
        //sparQlNodeRsp.setUri();
        sparQlNodeRsp.setValue(nodeBean.getValue());
        return sparQlNodeRsp;
    }
}
