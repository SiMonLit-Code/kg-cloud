package com.plantdata.kgcloud.domain.audit.service.impl;

import com.plantdata.kgcloud.domain.audit.entity.ApiAudit;
import com.plantdata.kgcloud.domain.audit.repository.ApiAuditRepository;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditTopReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditUrlReq;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditRsp;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditTopRsp;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditUrlRsp;
import com.plantdata.kgcloud.domain.audit.rsp.AuditKgNameRsp;
import com.plantdata.kgcloud.domain.audit.service.ApiAuditService;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ApiAuditServiceImpl
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/6 13:26
 * @Version 3.0.0
 **/
@Service
public class ApiAuditServiceImpl implements ApiAuditService {
    @Autowired
    private ApiAuditRepository apiAuditRepository;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Autowired
    private GraphRepository graphRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logApiAudit(ApiAuditMessage apiAuditMessage) {
        ApiAudit apiAudit = ConvertUtils.convert(ApiAudit.class).apply(apiAuditMessage);
        apiAudit.setId(this.kgKeyGenerator.getNextId());
        apiAudit.setStatus(apiAuditMessage.getStatus().getCode());
        this.apiAuditRepository.save(apiAudit);
    }

    private <T extends ApiAuditReq> void setInvokeTime(T req) {
        if (StringUtils.isBlank(req.getFrom())) {
            req.setBeginTime(DateUtils.getStartTimeOfDate(System.currentTimeMillis()));
        }
        if (StringUtils.isBlank(req.getTo())) {
            req.setBeginTime(DateUtils.getEndTimeOfDate(System.currentTimeMillis()));
        }
    }

    @Override
    public List<AuditKgNameRsp> findAllKgName() {
        return graphRepository.findAll().stream()
                .map(ConvertUtils.convert(AuditKgNameRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApiAuditRsp> groupByKgName(ApiAuditReq req) {
        setInvokeTime(req);
        return null;
    }

    @Override
    public ApiAuditUrlRsp groupByUrl(ApiAuditUrlReq req) {
        setInvokeTime(req);
        return null;
    }

    @Override
    public List<ApiAuditTopRsp> groupByTop(ApiAuditTopReq req) {
        setInvokeTime(req);
        return null;
    }


    /*

     select page as name,count(*) as value from t_sdk where invoke_time BETWEEN #{from,jdbcType=TIMESTAMP} AND #{to,jdbcType=TIMESTAMP}
     <if test="urls != null and urls.size() > 0">
     and url in
     <foreach collection="urls" item="item" index="index" open="(" close=")" separator=",">
     #{item}
     </foreach>
     </if>
     <if test="url != null">
     and url = #{url}
     </if>
     <if test="kgName != null">
     and kg_name = #{kgName}
     </if>
     <if test="page != null">
     and page = #{page}
     </if>
     group by page order by value DESC

     */


    @Override
    public List<ApiAuditRsp> groupByPage(ApiAuditReq req) {
        setInvokeTime(req);
        Specification<ApiAudit> specification = new Specification<ApiAudit>() {
            @Override
            public Predicate toPredicate(Root<ApiAudit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.between(root.<Date>get("invokeAt"),req.getBeginTime(),req.getEndTime()));

                return predicate;
            }
        };
        apiAuditRepository.findAll(specification);
        return null;
    }
}
