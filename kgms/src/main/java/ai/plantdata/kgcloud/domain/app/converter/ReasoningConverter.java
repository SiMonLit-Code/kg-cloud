package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.semantic.rsp.NodeBean;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import ai.plantdata.kg.api.semantic.rsp.TripleBean;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-28 00:43
 **/
public class ReasoningConverter extends BasicConverter {

    public static CommonBasicGraphExploreRsp reasoningResult2CommonBasicGraphExplore(ReasoningResultRsp reasoningResultRsp) {

        CommonBasicGraphExploreRsp basic = new CommonBasicGraphExploreRsp();


        List<CommonEntityRsp> entityRspList = Lists.newArrayList();
        List<GraphRelationRsp> relationRspList = Lists.newArrayList();
        basic.setEntityList(entityRspList);
        basic.setRelationList(relationRspList);

        if(reasoningResultRsp.getTripleList() == null || reasoningResultRsp.getTripleList().isEmpty()){
            return basic;
        }
        NodeBean start = reasoningResultRsp.getTripleList().get(0).getStart();
        CommonEntityRsp startEntityRsp = new CommonEntityRsp();
        startEntityRsp.setId(start.getId());
        startEntityRsp.setType(start.getType());
        entityRspList.add(startEntityRsp);

        for(TripleBean tripleBean : reasoningResultRsp.getTripleList()){

            NodeBean nodeBean = tripleBean.getEnd();
            CommonEntityRsp entityRsp = new CommonEntityRsp();
            entityRsp.setId(nodeBean.getId());
            entityRsp.setType(nodeBean.getType());

            entityRspList.add(entityRsp);

            GraphRelationRsp relationRsp = new GraphRelationRsp();
            relationRsp.setAttName(tripleBean.getEdge().getName());
            relationRsp.setFrom(start.getId());
            relationRsp.setTo(nodeBean.getId());

            relationRspList.add(relationRsp);
        }

        return basic;
    }
}
