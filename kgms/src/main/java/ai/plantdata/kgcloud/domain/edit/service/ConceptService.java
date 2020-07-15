package ai.plantdata.kgcloud.domain.edit.service;

import ai.plantdata.kgcloud.domain.edit.req.basic.AdditionalModifyReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.ConceptReplaceReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.GisModifyReq;
import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:36
 * @Description:
 */
public interface ConceptService {

    /**
     * 概念树
     *
     * @param kgName
     * @param conceptId
     * @return
     */
    List<BasicInfoVO> getConceptTree(String kgName, Long conceptId);

    /**
     * 更新gis
     *
     * @param kgName
     * @param gisModifyReq
     * @return
     */
    void updateGis(String kgName, GisModifyReq gisModifyReq);

    /**
     * 更新业务额外信息
     *
     * @param kgName
     * @param additionalModifyReq
     * @return
     */
    void updateAdditional(String kgName, AdditionalModifyReq additionalModifyReq);

    /**
     * 修改父概念
     *
     * @param kgName
     * @param conceptReplaceReq
     */
    void replaceConceptId(String kgName, ConceptReplaceReq conceptReplaceReq);

}
