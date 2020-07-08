package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationDetailRsp;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import com.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.SimpleBasicRsp;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 11:39
 * @Description:
 */
public interface BasicInfoService {

    /**
     * 添加概念或实体
     *
     * @param kgName
     * @param basicInfoReq
     * @return
     */
    Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq);

    /**
     * 通用建模库
     *
     * @param kgName
     * @param basicInfoReq
     * @param serviceEnum
     * @return
     */
    Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq, ServiceEnum serviceEnum);

    /**
     * 删除概念或实体
     *
     * @param kgName
     * @param id
     * @return
     */
    void deleteBasicInfo(String kgName, Long id, Boolean force);

    /**
     * 更新概念或实体名称,消歧标识,唯一标示
     *
     * @param kgName
     * @param basicInfoModifyReq
     * @return
     */
    void updateBasicInfo(String kgName, BasicInfoModifyReq basicInfoModifyReq);

    /**
     * 概念或实体详情
     *
     * @param kgName
     * @param basicReq
     * @return
     */
    BasicInfoRsp getDetails(String kgName, BasicReq basicReq);

    /**
     * 多模态数据
     *
     * @param kgName
     * @param entityId
     * @return
     */
    List<MultiModalRsp> listMultiModels(String kgName, Long entityId);
    /**
     * 批量查询多模态数据
     *
     * @param kgName
     * @param entityIds
     * @return
     */
    Map<Long,List<MultiModalRsp>> listMultiModels(String kgName, List<Long> entityIds);

    /**
     * 批量查询知识标引
     * @param kgName
     * @param entityIds
     * @return
     */
    Map<Long, List<KnowledgeIndexRsp>> listKnowledgeIndexs(String kgName, List<Long> entityIds);


    /**
     * 更新概念或实体摘要
     *
     * @param kgName
     * @param abstractModifyReq
     * @return
     */
    void updateAbstract(String kgName, AbstractModifyReq abstractModifyReq);

    /**
     * 批量获取概念或实体详情
     *
     * @param kgName
     * @param ids
     * @return
     */
    List<BasicInfoRsp> listByIds(String kgName, List<Long> ids);

    /**
     * 添加概念或实体的同义词
     *
     * @param kgName
     * @param synonymReq
     */
    void addSynonym(String kgName, SynonymReq synonymReq);

    /**
     * 删除概念或实体的同义词
     *
     * @param kgName
     * @param synonymReq
     */
    void deleteSynonym(String kgName, SynonymReq synonymReq);

    /**
     * 保存图片路径
     *
     * @param kgName
     * @param imageUrlReq
     */
    void saveImageUrl(String kgName, ImageUrlReq imageUrlReq);

    /**
     * 概念实体同义属性提示
     *
     * @param kgName
     * @param promptReq
     * @return
     */
    List<PromptRsp> prompt(String kgName, PromptReq promptReq);

    /**
     * 图谱统计
     *
     * @param kgName
     * @return
     */
    GraphStatisRsp graphStatis(String kgName);

    /**
     * 批量保存额外信息
     *
     * @param kgName
     * @param additionalReq
     */
    void batchAddMetaData(String kgName, AdditionalReq additionalReq);

    /**
     * 一键清空额外信息
     *
     * @param kgName
     */
    void clearMetaData(String kgName);

    /**
     * kgql
     *
     * @param kgqlReq
     * @return
     */
    Object executeQl(KgqlReq kgqlReq);
//    Object executeQl(String query);

    List<SimpleBasicRsp> listNames(String kgName, List<String> names);

    /**
     * 根据三元组id查关系详情
     *
     * @param id
     * @return
     */
    RelationDetailRsp getRelationDetails(String kgName, String id);
}
