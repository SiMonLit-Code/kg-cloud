package ai.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import ai.plantdata.kgcloud.sdk.req.app.PromptReq;
import ai.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import ai.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/22 15:36
 */
public interface GraphPromptService {
    /**
     * 综合搜索
     *
     * @param kgName    kgName
     * @param promptReq body
     * @return list
     */
    List<PromptEntityRsp> prompt(String kgName, PromptReq promptReq);

    /**
     * 高级搜索查询实体
     * @param kgName kgName
     * @param seniorPromptReq body
     * @return list
     */
    List<SeniorPromptRsp> seniorPrompt(String kgName, SeniorPromptReq seniorPromptReq);

    /**
     * 边属性搜索提示
     * @param kgName kgName
     * @param promptReq 边属性搜索的参数
     * @return list
     */
    List<EdgeAttributeRsp> edgeAttributeSearch(String kgName, EdgeAttrPromptReq promptReq);
}
