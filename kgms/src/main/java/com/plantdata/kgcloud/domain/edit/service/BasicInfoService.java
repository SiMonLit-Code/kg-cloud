package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.KgqlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;

import java.util.List;

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
     * 删除概念或实体
     *
     * @param kgName
     * @param id
     * @return
     */
    void deleteBasicInfo(String kgName, Long id,Boolean force);

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

}
