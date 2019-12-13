package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchPrivateRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteEdgeObjectReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeletePrivateDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeNumericAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityMetaDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTagSearchReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTimeModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.GisInfoModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.NumericalAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.ObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.PrivateAttrDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.SsrModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.UpdateRelationMetaReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.vo.EntityLinkVO;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 10:12
 * @Description:
 */
public interface EntityService {

    /**
     * 添加概念
     *
     * @param kgName
     * @param conceptId
     * @param entityId
     */
    void addMultipleConcept(String kgName, Long conceptId, Long entityId);

    /**
     * 删除概念
     *
     * @param kgName
     * @param conceptId
     * @param entityId
     */
    void deleteMultipleConcept(String kgName, Long conceptId, Long entityId);

    /**
     * 实体列表
     *
     * @param kgName
     * @param basicInfoListReq
     * @return
     */
    Page<BasicInfoRsp> listEntities(String kgName, BasicInfoListReq basicInfoListReq);

    /**
     * 批量删除实体
     *
     * @param kgName
     * @param ids
     * @return
     */
    List<DeleteResult> deleteByIds(String kgName, List<Long> ids);


    /**
     * 删除概念下的实体
     *
     * @param kgName
     * @param entityDeleteReq
     * @return
     */
    void deleteByConceptId(String kgName, EntityDeleteReq entityDeleteReq);

    /**
     * 修改实体权重,来源,置信度
     *
     * @param kgName
     * @param entityId
     * @param ssrModifyReq
     * @return
     */
    void updateScoreSourceReliability(String kgName, Long entityId, SsrModifyReq ssrModifyReq);

    /**
     * 修改实体开始,截止时间
     *
     * @param kgName
     * @param entityId
     * @param entityTimeModifyReq
     * @return
     */
    void updateEntityTime(String kgName, Long entityId, EntityTimeModifyReq entityTimeModifyReq);

    /**
     * 修改实体gis信息
     *
     * @param kgName
     * @param entityId
     * @param gisInfoModifyReq
     * @return
     */
    void updateGisInfo(String kgName, Long entityId, GisInfoModifyReq gisInfoModifyReq);

    /**
     * 根据来源 ,批次号删除实体
     *
     * @param kgName
     * @param entityMetaDeleteReq
     * @return
     */
    void deleteByMeta(String kgName, EntityMetaDeleteReq entityMetaDeleteReq);

    /**
     * 添加实体标签
     *
     * @param kgName
     * @param entityId
     * @param vos
     * @return
     */
    void addEntityTag(String kgName, Long entityId, List<EntityTagVO> vos);

    /**
     * 修改实体标签
     *
     * @param kgName
     * @param entityId
     * @param vos
     * @return
     */
    void updateEntityTag(String kgName, Long entityId, List<EntityTagVO> vos);

    /**
     * 删除实体标签
     *
     * @param kgName
     * @param entityId
     * @param tagNames
     * @return
     */
    void deleteEntityTag(String kgName, Long entityId, List<String> tagNames);

    /**
     * 添加实体关联
     *
     * @param kgName
     * @param entityId
     * @param vos
     * @return
     */
    void addEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos);

    /**
     * 删除实体关联
     *
     * @param kgName
     * @param entityId
     * @param vos
     * @return
     */
    void deleteEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos);

    /**
     * 数值属性值更新
     *
     * @param kgName
     * @param numericalAttrValueReq
     */
    void upsertNumericalAttrValue(String kgName, NumericalAttrValueReq numericalAttrValueReq);

    /**
     * 添加对象属性值
     *
     * @param kgName
     * @param objectAttrValueReq
     */
    void addObjectAttrValue(String kgName, ObjectAttrValueReq objectAttrValueReq);

    /**
     * 修改关系的metadata(权重,来源,置信度,来源理由,关系时间)
     *
     * @param kgName
     * @param updateRelationMetaReq
     */
    void updateRelationMeta(String kgName, UpdateRelationMetaReq updateRelationMetaReq);

    /**
     * 删除对象属性值
     *
     * @param kgName
     * @param deleteRelationReq
     */
    void deleteObjAttrValue(String kgName, DeleteRelationReq deleteRelationReq);

    /**
     * 添加私有数值或对象属性值
     *
     * @param kgName
     * @param privateAttrDataReq
     */
    void addPrivateData(String kgName, PrivateAttrDataReq privateAttrDataReq);

    /**
     * 批量删除私有数值或对象属性值
     *
     * @param kgName
     * @param deletePrivateDataReq
     */
    void deletePrivateData(String kgName, DeletePrivateDataReq deletePrivateDataReq);

    /**
     * 添加或更新边数值属性值
     *
     * @param kgName
     * @param edgeNumericAttrValueReq
     */
    void addEdgeNumericAttrValue(String kgName, EdgeNumericAttrValueReq edgeNumericAttrValueReq);

    /**
     * 添加边对象属性值
     *
     * @param kgName
     * @param edgeObjectAttrValueReq
     */
    void addEdgeObjectAttrValue(String kgName, EdgeObjectAttrValueReq edgeObjectAttrValueReq);

    /**
     * 删除边对象属性值
     *
     * @param kgName
     * @param deleteEdgeObjectReq
     */
    void deleteEdgeObjectAttrValue(String kgName, DeleteEdgeObjectReq deleteEdgeObjectReq);

    /**
     * 批量添加对象属性值
     *
     * @param kgName
     * @param batchRelationReq
     * @return 关系的id
     */
    List<String> batchAddRelation(String kgName, BatchRelationReq batchRelationReq);

    /**
     * 批量添加私有对象属性值
     *
     * @param kgName
     * @param batchPrivateRelationReq
     * @return
     */
    List<String> batchAddPrivateRelation(String kgName, BatchPrivateRelationReq batchPrivateRelationReq);

    /**
     * 实体标签搜索
     *
     * @param kgName
     * @param entityTagSearchReq
     * @return
     */
    List<String> tagSearch(String kgName, EntityTagSearchReq entityTagSearchReq);

    List<OpenEntityRsp> queryEntityList(String kgName, EntityQueryReq entityQueryReq);

    List<OpenBatchSaveEntityRsp> saveOrUpdate(String kgName, boolean update, List<OpenBatchSaveEntityRsp> batchEntity);

    void batchDeleteEntityAttr(String kgName, BatchEntityAttrDeleteReq deleteReq);
}
