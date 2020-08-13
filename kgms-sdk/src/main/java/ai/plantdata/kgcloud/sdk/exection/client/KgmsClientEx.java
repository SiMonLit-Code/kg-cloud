package ai.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.sdk.req.*;
import ai.plantdata.kgcloud.sdk.rsp.*;
import ai.plantdata.kgcloud.sdk.KgmsClient;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:18
 **/
@Component
public class KgmsClientEx implements KgmsClient {
    @Override
    public ApiReturn<GraphConfSnapshotRsp> saveSnapShot(GraphConfSnapshotReq graphConfSnapshotReq) {
         return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteSnapShot(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphConfSnapshotRsp>> findAllSnapShot(String kgName, String spaId, Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }



    @Override
    public ApiReturn<GraphConfSnapshotRsp> findByIdSnapShot(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphRsp>> graphFindAll() {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphRsp>> graphFindAll(String kw, Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphRsp> graphFindById(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<String> graphFindDbNameByKgName(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphRsp> graphInsert(@Valid GraphReq dictionaryReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphRsp> graphUpdate(String kgName, @Valid GraphReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn graphDelete(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<LinkShareSpaRsp> shareStatus(String kgName, String spaId) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<SelfSharedRsp> shareSpaStatus(String kgName, String spaId, String token) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfAlgorithmRsp> save(String kgName, @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfAlgorithmRsp> update(Long id, @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn delete(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphConfAlgorithmRsp>> select(String kgName, Integer type, Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfFocusRsp>> find(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfFocusRsp>> saveFocus(String kgName, @Valid List<GraphConfFocusReq> req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfKgqlRsp> saveKgql(String kgName, @Valid GraphConfKgqlReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfKgqlRsp> updateKgql(Long id, @Valid GraphConfKgqlReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteKgql(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphConfKgqlRsp>> selectKgql(String kgName, Integer ruleType, Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfKgqlRsp> detailKgql(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfQaRsp>> save(String kgName, @Valid List<GraphConfQaReq> req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfQaRsp>> selectQa(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfStatisticalRsp> saveStatistical(String kgName, @Valid GraphConfStatisticalReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfStatisticalRsp>> saveStatisticalBatch(@Valid List<GraphConfStatisticalReq> listReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfStatisticalRsp> updateStatistical(Long id, @Valid GraphConfStatisticalReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfStatisticalRsp>> updateStatisticalBatch(@Valid List<UpdateGraphConfStatisticalReq> reqs) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteStatistical(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteStatisticalBatch(List<Long> ids) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphConfStatisticalRsp>> selectStatisticalPage(String kgName, BaseReq baseReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfReasonRsp> saveReasoning(String kgName, @Valid GraphConfReasonReq req) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasePage<GraphConfReasonRsp>> selectReasoningPage(String kgName, Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfReasonRsp> detailReasoning(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn deleteReasoning(Long id) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphConfReasonRsp> updateReasoning(Long id, @Valid GraphConfReasonReq req) {
        return ApiReturn.fail(500,"请求超时");
    }
}
