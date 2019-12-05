package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.GisModifyReq;
import com.plantdata.kgcloud.domain.edit.vo.BasicInfoVO;

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

}
