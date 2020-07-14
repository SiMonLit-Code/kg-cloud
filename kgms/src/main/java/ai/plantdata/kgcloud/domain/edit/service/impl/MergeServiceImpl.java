package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.MergeApi;
import ai.plantdata.kg.api.edit.merge.EntityMergeSourceVO;
import ai.plantdata.kg.api.edit.merge.MergeEntity4Edit;
import ai.plantdata.kg.api.edit.merge.MergeEntityDetail;
import ai.plantdata.kg.api.edit.merge.MergeFinalEntityFrom;
import ai.plantdata.kg.api.edit.merge.WaitMergeVO;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.req.merge.WaitMergeReq;
import ai.plantdata.kgcloud.domain.edit.rsp.MergeEntityDetailRsp;
import ai.plantdata.kgcloud.domain.edit.service.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 15:03
 * @Description:
 */
@Service
public class MergeServiceImpl implements MergeService {

    @Autowired
    private MergeApi mergeApi;


    @Override
    public Set<String> allSource(String kgName) {
        return RestRespConverter.convert(mergeApi.allSource(KGUtil.dbName(kgName))).orElse(Collections.emptySet());
    }

    @Override
    public List<EntityMergeSourceVO> getSourceSort(String kgName) {
        return RestRespConverter.convert(mergeApi.getSourceSort(KGUtil.dbName(kgName)))
                .orElse(Collections.emptyList());
    }

    @Override
    public void saveSourceSort(String kgName, Map<Integer, String> sourceList) {
        RestRespConverter.convertVoid(mergeApi.saveSourceSort(KGUtil.dbName(kgName), sourceList));
    }

    @Override
    public void mergeByObjIds(String kgName, Integer mode, List<String> objIds) {
        RestRespConverter.convertVoid(mergeApi.mergeByObjIds(KGUtil.dbName(kgName), mode, objIds));
    }

    @Override
    public void doMergeEntity(String kgName, String objId, MergeFinalEntityFrom save) {
        RestRespConverter.convertVoid(mergeApi.doMergeEntity(KGUtil.dbName(kgName), objId, save));
    }


    @Override
    public String createMergeEntity(String kgName, Set<Long> ids) {
        return RestRespConverter.convert(mergeApi.createMergeEntity(KGUtil.dbName(kgName), new ArrayList<>(ids)))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS));
    }

    @Override
    public void insertMergeEntity(String kgName, String objId, List<Long> ids) {
        RestRespConverter.convertVoid(mergeApi.insertMergeEntity(KGUtil.dbName(kgName), objId, ids));
    }

    @Override
    public void deleteMergeEntity(String kgName, String objId, Collection<Long> ids) {
        RestRespConverter.convertVoid(mergeApi.deleteMergeEntity(KGUtil.dbName(kgName), objId, new ArrayList<>(ids)));
    }

    @Override
    public Page<WaitMergeVO> waitList(String kgName, WaitMergeReq req) {
        RestResp<List<WaitMergeVO>> listRestResp = mergeApi.waitList(KGUtil.dbName(kgName), req.getOffset(), req.getLimit(),req.getJobId());
        List<WaitMergeVO> list = RestRespConverter.convert(listRestResp).orElse(Collections.emptyList());
        Integer integer = RestRespConverter.convertCount(listRestResp).orElse(0);
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        return new PageImpl<>(list, pageable, integer);
    }

    @Override
    public void deleteWaitList(String kgName, List<String> ids) {
        RestRespConverter.convertVoid(mergeApi.deleteWaitList(KGUtil.dbName(kgName), ids));
    }

    @Override
    public List<MergeEntityDetailRsp> showEntityList(String kgName, String objId) {
        RestResp<List<MergeEntityDetail>> listRestResp = mergeApi.showEntityList(KGUtil.dbName(kgName), objId);
        List<MergeEntityDetail> mergeEntityDetails = RestRespConverter.convert(listRestResp).orElse(Collections.emptyList());
        return mergeEntityDetails.stream().map(ConvertUtils.convert(MergeEntityDetailRsp.class)).peek((s) -> {
            Map<String, Object> metaData = s.getMetaData();
            if (metaData != null && !metaData.isEmpty()) {
                if (metaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    s.setSource(metaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
                }
                if (metaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    s.setReliability(metaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString());
                }
            }
        }).collect(Collectors.toList());
    }

    @Override
    public MergeEntity4Edit showDifferent(String kgName, String objId, Integer mode) {
        return RestRespConverter.convert(mergeApi.showDifferent(KGUtil.dbName(kgName), objId, mode))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS));
    }
}
