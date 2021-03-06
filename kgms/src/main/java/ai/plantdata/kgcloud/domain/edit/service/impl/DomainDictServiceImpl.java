package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.DomainDicApi;
import ai.plantdata.kg.api.edit.req.DomainFrom;
import ai.plantdata.kg.api.edit.resp.DomainDicVO;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import cn.hiboot.mcn.core.model.result.RestResp;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.req.dict.DictReq;
import ai.plantdata.kgcloud.domain.edit.req.dict.DictSearchReq;
import ai.plantdata.kgcloud.domain.edit.rsp.DictRsp;
import ai.plantdata.kgcloud.domain.edit.service.DomainDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 14:15
 * @Description:
 */
@Service
public class DomainDictServiceImpl implements DomainDictService {

    @Autowired
    private DomainDicApi domainDicApi;

    @Override
    public void batchInsert(String kgName, List<DictReq> dictReqs) {
        //TODO 待优化
        if (CollectionUtils.isEmpty(dictReqs)) {
            throw BizException.of(KgmsErrorCodeEnum.DOMAIN_WORD_NOT_EMPTY);
        }
        List<DomainFrom> domainFroms = new ArrayList<>();
        for (DictReq dictReq : dictReqs) {
            if (dictReq.getConceptId() == null) {
                throw BizException.of(KgmsErrorCodeEnum.DOMAIN_CONCEPT_NOT_EMPTY);
            }
            if (!StringUtils.hasText(dictReq.getName())) {
                throw BizException.of(KgmsErrorCodeEnum.DOMAIN_WORD_NOT_EMPTY);
            }
            domainFroms.add(ConvertUtils.convert(DomainFrom.class).apply(dictReq));
        }
        RestRespConverter.convertVoid(domainDicApi.batchSave(KGUtil.dbName(kgName), domainFroms));
    }

    @Override
    public void update(String kgName, String id, DictReq dictReq) {
        DomainFrom domainFrom = ConvertUtils.convert(DomainFrom.class).apply(dictReq);
        RestRespConverter.convertVoid(domainDicApi.update(KGUtil.dbName(kgName), id, domainFrom));
    }

    @Override
    public void batchDelete(String kgName, List<String> ids) {
        RestRespConverter.convertVoid(domainDicApi.delete(KGUtil.dbName(kgName), ids));
    }

    @Override
    public Page<DictRsp> listDict(String kgName, DictSearchReq dictSearchReq) {
        Integer page = dictSearchReq.getPage();
        Integer size = dictSearchReq.getSize();
        Integer skip = (page - 1) * size;
        RestResp<List<DomainDicVO>> restResp = domainDicApi.list(KGUtil.dbName(kgName),
                dictSearchReq.getConceptId(), null, dictSearchReq.getFrequency(), skip, size);
        Optional<List<DomainDicVO>> optional = RestRespConverter.convert(restResp);
        List<DictRsp> dictRsps =
                optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(DictRsp.class)).collect(Collectors.toList());
        Optional<Integer> count = RestRespConverter.convertCount(restResp);
        return new PageImpl<>(dictRsps, PageRequest.of(page - 1, size), count.orElse(0));
    }

    @Override
    public List<ai.plantdata.kgcloud.sdk.rsp.edit.DictRsp> listDictByEntity(String kgName, Long entityId) {
        RestResp<List<DomainDicVO>> restResp = domainDicApi.list(KGUtil.dbName(kgName), null, entityId, 1, 0, 100);
        Optional<List<DomainDicVO>> optional = RestRespConverter.convert(restResp);
        return optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(ai.plantdata.kgcloud.sdk.rsp.edit.DictRsp.class)).collect(Collectors.toList());
    }
}
