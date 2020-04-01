package com.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.kg.api.pub.QlApi;
import ai.plantdata.kg.api.pub.resp.QuerySetting;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfKgql;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfKgqlRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfKgqlService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 图谱业务配置
 *
 * @author plantdata-1007
 * @date 2019/12/2
 */
@Service
public class GraphConfKgqlServiceImpl implements GraphConfKgqlService {
    @Autowired
    private GraphConfKgqlRepository graphConfKgqlRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Autowired
    private QlApi qlApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfKgqlRsp createKgql(String kgName, GraphConfKgqlReq req) {
        GraphConfKgql targe = new GraphConfKgql();
        BeanUtils.copyProperties(req, targe);
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        if (!targe.getKgql().isEmpty()) {
            RestResp<QuerySetting> restResp = qlApi.business(KGUtil.dbName(kgName), targe.getKgql());
            if (RestResp.ActionStatusMethod.FAIL.equals(restResp.getActionStatus())) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_KGQLQUERYSETTING_ERROR);
            }
            Optional<QuerySetting> convert = RestRespConverter.convert(restResp);
            if (!convert.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.QUERYSETTING_NOT_EXISTS);
            }
            if (Objects.isNull(convert.get().getDomain())) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_KGQLQUERYSETTING_ERROR);
            }
            String s = JacksonUtils.writeValueAsString(convert.get());
            targe.setRuleSettings(s);
        }
        GraphConfKgql result = graphConfKgqlRepository.save(targe);
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(result);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfKgqlRsp updateKgql(Long id, GraphConfKgqlReq req) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        if (!req.getKgql().isEmpty()) {
            RestResp<QuerySetting> restResp = qlApi.business(KGUtil.dbName(graphConfKgql.getKgName()), req.getKgql());
            if (RestResp.ActionStatusMethod.FAIL.equals(restResp.getActionStatus())) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_KGQLQUERYSETTING_ERROR);
            }
            Optional<QuerySetting> convert = RestRespConverter.convert(restResp);
            if (!convert.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.QUERYSETTING_NOT_EXISTS);
            }
            if (Objects.isNull(convert.get().getDomain())) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_KGQLQUERYSETTING_ERROR);
            }
            String s = JacksonUtils.writeValueAsString(convert.get());
            graphConfKgql.setRuleSettings(s);
        }
        BeanUtils.copyProperties(req, graphConfKgql);
        GraphConfKgql result = graphConfKgqlRepository.save(graphConfKgql);
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteKgql(Long id) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        graphConfKgqlRepository.delete(graphConfKgql);
    }

    @Override
    public Page<GraphConfKgqlRsp> findByKgNameAndRuleType(String kgName, Integer ruleType, BaseReq baseReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), sort);
        Page<GraphConfKgql> all = graphConfKgqlRepository.findByKgNameAndRuleType(kgName, ruleType, pageable);
        return all.map(ConvertUtils.convert(GraphConfKgqlRsp.class));
    }

    @Override
    public GraphConfKgqlRsp findById(Long id) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(graphConfKgql);
    }
}
