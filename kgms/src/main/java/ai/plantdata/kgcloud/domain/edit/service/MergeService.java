package ai.plantdata.kgcloud.domain.edit.service;

import ai.plantdata.kg.api.edit.merge.EntityMergeSourceVO;
import ai.plantdata.kg.api.edit.merge.MergeEntity4Edit;
import ai.plantdata.kg.api.edit.merge.MergeFinalEntityFrom;
import ai.plantdata.kg.api.edit.merge.WaitMergeVO;
import ai.plantdata.kgcloud.domain.edit.req.merge.WaitMergeReq;
import ai.plantdata.kgcloud.domain.edit.rsp.MergeEntityDetailRsp;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 14:17
 * @Description:
 */
public interface MergeService {
    /**
     * 获取所有来源
     *
     * @param kgName
     * @return
     */
    Set<String> allSource(String kgName);

    /**
     * 获取融合排序
     *
     * @param kgName
     * @return
     */
    List<EntityMergeSourceVO> getSourceSort(String kgName);

    /**
     * 保存来源排序
     *
     * @param kgName
     * @param sourceList
     */
    void saveSourceSort(String kgName, Map<Integer, String> sourceList);

    /**
     * 批量融合实体
     *
     * @param kgName
     * @param objIds
     * @param mode
     */
    void mergeByObjIds(String kgName, Integer mode, List<String> objIds);

    /**
     * 执行融合
     *
     * @param kgName
     * @param objId
     * @param save
     */
    void doMergeEntity(String kgName, String objId, MergeFinalEntityFrom save);

    /**
     * 手工融合实体
     *
     * @param kgName
     * @param ids
     * @return
     */
    String createMergeEntity(String kgName, Set<Long> ids);

    /**
     * 插入待融合实体
     *
     * @param kgName
     * @param objId
     * @param ids
     */
    void insertMergeEntity(String kgName, String objId, List<Long> ids);

    /**
     * 删除待融合实体
     *
     * @param kgName
     * @param objId
     * @param ids
     */
    void deleteMergeEntity(String kgName, String objId, Collection<Long> ids);

    /**
     * 待融合列表
     *
     * @param kgName
     * @param req
     * @return
     */
    Page<WaitMergeVO> waitList(String kgName, WaitMergeReq req);

    /**
     * 删除待融合列表
     *
     * @param kgName
     * @param ids
     */
    void deleteWaitList(String kgName, List<String> ids);

    /**
     * 获取待融合列表详情
     *
     * @param kgName
     * @param objId
     * @return
     */
    List<MergeEntityDetailRsp> showEntityList(String kgName, String objId);

    /**
     * 获取待融合列表详情
     *
     * @param kgName
     * @param objId
     * @param mode
     * @return
     */
    MergeEntity4Edit showDifferent(String kgName, String objId, Integer mode);
}
