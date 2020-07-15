package ai.plantdata.kgcloud.domain.edit.service;

import ai.plantdata.kgcloud.domain.edit.req.induce.AttrInduceReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.AttrSearchReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceConceptReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceMergeReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InduceObjectReq;
import ai.plantdata.kgcloud.domain.edit.req.induce.InducePublicReq;
import ai.plantdata.kgcloud.domain.edit.rsp.AttrInduceFindRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.InduceConceptRsp;
import ai.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 18:11
 * @Description:
 */
public interface InduceService {

    /**
     * 读取私有属性列表，按数量排序
     *
     * @param kgName
     * @param attrSearchReq
     * @return
     */
    Page<AttrDefinitionVO> induceAttributeList(String kgName, AttrSearchReq attrSearchReq);

    /**
     * 寻找待规约的属性
     *
     * @param kgName
     * @param attrInduceReq
     * @return
     */
    List<AttrInduceFindRsp> induceAttributeFind(String kgName, AttrInduceReq attrInduceReq);

    /**
     * 执行属性公有化
     *
     * @param kgName
     * @param inducePublicReq
     */
    void inducePublic(String kgName, InducePublicReq inducePublicReq);

    /**
     * 执行属性对象化
     *
     * @param kgName
     * @param induceObjectReq
     */
    void induceObject(String kgName, InduceObjectReq induceObjectReq);

    /**
     * 执行属性合并
     *
     * @param kgName
     * @param induceMergeReq
     */
    void induceMerge(String kgName, InduceMergeReq induceMergeReq);

    /**
     * 计算概念待规约列表
     *
     * @param kgName
     * @param conceptId
     * @return
     */
    List<InduceConceptRsp> listInduceConcept(String kgName, Long conceptId);

    /**
     * 执行概念规约
     *
     * @param kgName
     * @param induceConceptReq
     */
    void induceConcept(String kgName, InduceConceptReq induceConceptReq);
}
