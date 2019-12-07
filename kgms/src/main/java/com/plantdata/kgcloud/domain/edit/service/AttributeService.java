package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.attr.AttrConstraintsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.entity.TripleReq;
import com.plantdata.kgcloud.domain.edit.rsp.TripleRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionConceptsReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.domain.edit.req.attr.EdgeAttrDefinitionReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationAdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationMetaReq;
import com.plantdata.kgcloud.domain.edit.req.attr.RelationSearchReq;
import com.plantdata.kgcloud.domain.edit.rsp.AttrConstraintsRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionBatchRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 10:05
 * @Description:
 */
public interface AttributeService {

    /**
     * 属性定义详情
     *
     * @param kgName
     * @param id
     * @return
     */
    AttrDefinitionVO getAttrDetails(String kgName, Integer id);

    /**
     * 查询概念下的属性定义
     *
     * @param kgName
     * @param attrDefinitionSearchReq
     * @return
     */
    List<AttrDefinitionRsp> getAttrDefinitionByConceptId(String kgName,
                                                         AttrDefinitionSearchReq attrDefinitionSearchReq);

    /**
     * 根据实体查询属性定义
     *
     * @param kgName
     * @param entityId
     * @return
     */
    List<AttrDefinitionRsp> getAttrDefinitionByEntityId(String kgName, Long entityId);

    /**
     * 查询多概念下的属性定义
     *
     * @param kgName
     * @param attrDefinitionConceptsReq
     * @return
     */
    List<AttrDefinitionRsp> getAttrDefinitionByConceptIds(String kgName,
                                                          AttrDefinitionConceptsReq attrDefinitionConceptsReq);

    /**
     * 添加属性定义
     *
     * @param kgName
     * @param attrDefinitionReq
     * @return
     */
    Integer addAttrDefinition(String kgName, AttrDefinitionReq attrDefinitionReq);

    /**
     * 批量添加属性定义
     *
     * @param kgName
     * @param attrDefinitionReqs
     * @return
     */
    List<AttrDefinitionBatchRsp> batchAddAttrDefinition(String kgName, List<AttrDefinitionReq> attrDefinitionReqs);

    /**
     * 修改属性定义
     *
     * @param kgName
     * @param modifyReq
     */
    void updateAttrDefinition(String kgName, AttrDefinitionModifyReq modifyReq);

    /**
     * 删除属性定义
     *
     * @param kgName
     * @param id
     */
    void deleteAttrDefinition(String kgName, Integer id);

    /**
     * 添加边属性定义
     *
     * @param kgName
     * @param attrId
     * @param edgeAttrDefinitionReq
     */
    void addEdgeAttr(String kgName, Integer attrId, EdgeAttrDefinitionReq edgeAttrDefinitionReq);

    /**
     * 修改边属性定义
     *
     * @param kgName
     * @param attrId
     * @param seqNo
     * @param edgeAttrDefinitionReq
     */
    void updateEdgeAttr(String kgName, Integer attrId, Integer seqNo, EdgeAttrDefinitionReq edgeAttrDefinitionReq);

    /**
     * 删除边属性定义
     *
     * @param kgName
     * @param attrId 属性id
     * @param seqNo  边属性id
     */
    void deleteEdgeAttr(String kgName, Integer attrId, Integer seqNo);

    /**
     * 根据属性模板添加属性定义
     *
     * @param kgName
     * @param attrTemplateReqs
     */
    void addAttrDefinitionTemplate(String kgName, List<AttrTemplateReq> attrTemplateReqs);

    /**
     * 关系溯源
     *
     * @param kgName
     * @param relationSearchReq
     * @return
     */
    Page<RelationRsp> listRelations(String kgName, RelationSearchReq relationSearchReq);

    /**
     * 批量删除关系
     *
     * @param kgName
     * @param tripleIds
     */
    void deleteRelations(String kgName, List<String> tripleIds);

    /**
     * 根据meta删除关系
     *
     * @param kgName
     * @param relationMetaReq
     */
    void deleteRelationByMeta(String kgName, RelationMetaReq relationMetaReq);

    /**
     * 添加或更新关系的业务信息
     *
     * @param kgName
     * @param relationAdditionalReq
     */
    void upsertRelationAdditional(String kgName, RelationAdditionalReq relationAdditionalReq);

    /**
     * 修改对象属性定义的业务信息
     *
     * @param kgName
     * @param additionalReq
     */
    void updateAttrDefinitionAdditional(String kgName, AttrDefinitionAdditionalReq additionalReq);

    /**
     * 不满足属性约束列表
     *
     * @param kgName
     * @param attrConstraintsReq
     * @return
     */
    List<AttrConstraintsRsp> listAttrConstraints(String kgName, AttrConstraintsReq attrConstraintsReq);

    /**
     * 批量删除不满足属性约束的值
     *
     * @param kgName
     * @param attrId
     * @param tripleIds
     */
    void attrConstraintsDelete(String kgName, Integer attrId, List<String> tripleIds);

    /**
     * 根据属性获得三元组
     *
     * @param kgName
     * @param tripleReq
     * @return
     */
    List<TripleRsp> getRelationByAttr(String kgName, TripleReq tripleReq);
}
