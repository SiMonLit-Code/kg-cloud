package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.merge.EntityMergeReq;
import com.plantdata.kgcloud.domain.edit.req.merge.MergeRecommendedReq;
import com.plantdata.kgcloud.domain.edit.req.merge.RecommendedMergeReq;
import com.plantdata.kgcloud.domain.edit.rsp.EntityMergeRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 14:17
 * @Description:
 */
public interface MergeService {

    /**
     * 实体合并
     *
     * @param kgName
     * @param entityMergeReq
     */
    void entityMerge(String kgName, EntityMergeReq entityMergeReq);

    /**
     * 融合候选集列表
     *
     * @param kgName
     * @param mergeRecommendedReq
     * @return
     */
    Page<EntityMergeRsp> listMerges(String kgName, MergeRecommendedReq mergeRecommendedReq);

    /**
     * 推荐实体批量合并
     *
     * @param kgName
     * @param recommendedMergeReqs
     */
    void recommendedMerge(String kgName, List<RecommendedMergeReq> recommendedMergeReqs);
}
