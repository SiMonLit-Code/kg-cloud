package com.plantdata.kgcloud.domain.audit.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.audit.entity.ApiAudit;
import com.plantdata.kgcloud.domain.audit.repository.ApiAuditRepository;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditTopReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditUrlReq;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditRsp;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditTopRsp;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditUrlRsp;
import com.plantdata.kgcloud.domain.audit.rsp.ApiAuditVoRsp;
import com.plantdata.kgcloud.domain.audit.rsp.AuditKgNameRsp;
import com.plantdata.kgcloud.domain.audit.service.ApiAuditService;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logApiAudit(ApiAuditMessage apiAuditMessage) {
        ApiAudit apiAudit = ConvertUtils.convert(ApiAudit.class).apply(apiAuditMessage);
        apiAudit.setId(this.kgKeyGenerator.getNextId());
        apiAudit.setStatus(apiAuditMessage.getStatus().getCode());
        this.apiAuditRepository.save(apiAudit);
    }

    private <T extends ApiAuditReq> void setInvokeTime(T req) {
        if (StringUtils.hasText(req.getFrom())) {
            req.setBeginTime(DateUtils.parseDatetime(req.getFrom()));
        } else {
            req.setBeginTime(DateUtils.getStartTimeOfDate(System.currentTimeMillis()));
        }
        if (StringUtils.hasText(req.getTo())) {
            req.setEndTime(DateUtils.parseDatetime(req.getTo()));
        } else {
            req.setEndTime(DateUtils.getEndTimeOfDate(System.currentTimeMillis()));
        }
    }

    @Override
    public List<AuditKgNameRsp> findAllKgName() {
        return graphRepository.findAll().stream()
                .map(ConvertUtils.convert(AuditKgNameRsp.class))
                .collect(Collectors.toList());
    }

    /**
     * mybatis实现
     * <p>
     * select kg_name as name,count(*) as value from t_sdk where invoke_time BETWEEN #{from} AND #{to}
     * <if test="urls != null and urls.size() > 0">
     * and url in
     * <foreach collection="urls" item="item" index="index" open="(" close=")" separator=",">
     * #{item}
     * </foreach>
     * </if>
     * <if test="url != null">
     * and url = #{url}
     * </if>
     * <if test="kgName != null">
     * and kg_name = #{kgName}
     * </if>
     * <if test="page != null">
     * and page = #{page}
     * </if>
     * group by kg_name order by value DESC
     *
     * @param req
     * @return
     */

    @Override
    public List<ApiAuditRsp> groupByKgName(ApiAuditReq req) {
        setInvokeTime(req);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<ApiAudit> root = query.from(ApiAudit.class);
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        setApiQuery(expressions, root, cb, req);

        Path<String> page = root.<String>get("kgName");
        Selection<String> name = page.alias("name");
        Selection<Long> value = cb.count(page).alias("value");
        query.multiselect(name, value);
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(cb.count(page)));
        query.groupBy(page).orderBy(orderList);

        query.where(predicate);
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        List<Tuple> resultList = typedQuery.getResultList();
        List<ApiAuditRsp> auditRspList = new ArrayList<>();
        for (Tuple tuple : resultList) {
            ApiAuditRsp apiAuditRsp = new ApiAuditRsp();
            apiAuditRsp.setName((String) tuple.get(0));
            apiAuditRsp.setValue((Long) tuple.get(1));
            auditRspList.add(apiAuditRsp);
        }
        return auditRspList;
    }


    /**
     * mybatis实现
     * SELECT url as name,
     * <choose>
     * <when test="groupKey ==1">
     * TO_CHAR(invoke_time,'YYYY-MM') as time,
     * </when>
     * <when test="groupKey ==3">
     * TO_CHAR(invoke_time,'YYYY-MM-DD HH') as time,
     * </when>
     * <otherwise>
     * TO_CHAR(invoke_time,'YYYY-MM-DD') as time,
     * </otherwise>
     * </choose>
     * count(*) as value FROM t_sdk where invoke_time BETWEEN #{sdkLogDTO.from} AND #{sdkLogDTO.to}
     * <if test="sdkLogDTO.urls != null and sdkLogDTO.urls.size() > 0">
     * and url in
     * <foreach collection="sdkLogDTO.urls" item="item" index="index" open="(" close=")" separator=",">
     * #{item}
     * </foreach>
     * </if>
     * <if test="sdkLogDTO.kgName != null">
     * and kg_name = #{sdkLogDTO.kgName}
     * </if>
     * <if test="sdkLogDTO.page != null">
     * and page = #{sdkLogDTO.page}
     * </if>
     * GROUP BY time,url
     *
     * @param req
     * @return
     */
    @Override
    public ApiAuditUrlRsp groupByUrl(ApiAuditUrlReq req) {
        setInvokeTime(req);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<ApiAudit> root = query.from(ApiAudit.class);
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        Path<Date> invokeAt = root.<Date>get("invokeAt");

        setApiQuery(expressions, root, cb, req);

        Path<String> urlPath = root.<String>get("url");
        Selection<String> name = urlPath.alias("name");
        Expression<String> year = cb.function("year", String.class, invokeAt);
        Expression<String> month = cb.function("month", String.class, invokeAt);
        Expression<String> day = cb.function("day", String.class, invokeAt);
        Expression<String> hour = cb.function("hour", String.class, invokeAt);

        Expression<String> yearMonth;

        if (Objects.equals(req.getGroup(), 1)) {
            yearMonth = cb.concat(cb.concat(year, "-"), month);
        } else if (Objects.equals(req.getGroup(), 3)) {
            yearMonth = cb.concat(cb.concat(cb.concat(cb.concat(cb.concat(cb.concat(year, "-"), month), "-"), day), " "), hour);
        } else {
            yearMonth = cb.concat(cb.concat(cb.concat(cb.concat(year, "-"), month), "-"), day);
        }
        Selection<String> timeAlias = yearMonth.alias("time");
        Selection<Long> value = cb.count(urlPath).alias("value");
        query.multiselect(name, timeAlias, value);
        query.groupBy(yearMonth, urlPath);
        query.where(predicate);
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        List<Tuple> resultList = typedQuery.getResultList();

        List<ApiAuditVoRsp> auditVoRspList = new ArrayList<>();
        for (Tuple tuple : resultList) {
            ApiAuditVoRsp apiAuditVoRsp = new ApiAuditVoRsp();
            apiAuditVoRsp.setName((String) tuple.get(0));
            apiAuditVoRsp.setTime((String) tuple.get(1));
            apiAuditVoRsp.setValue((Long) tuple.get(2));
            auditVoRspList.add(apiAuditVoRsp);
        }

        Map<String, List<ApiAuditVoRsp>> rs = auditVoRspList.stream().collect(Collectors.groupingBy(ApiAuditVoRsp::getTime));
        List<String> times = Lists.newArrayList(rs.keySet());
        Collections.sort(times);
        Map<String, List<ApiAuditVoRsp>> collect = auditVoRspList.stream().collect(Collectors.groupingBy(ApiAuditVoRsp::getName));

        List<ApiAuditUrlRsp.VerticalData> vertical = Lists.newArrayList();
        ApiAuditUrlRsp sdkLogGroupUrlVO = new ApiAuditUrlRsp(times, vertical);

        collect.forEach((url, sdkLogTopVoList) -> {
            ApiAuditUrlRsp.VerticalData m = new ApiAuditUrlRsp.VerticalData();
            m.setName(url);
            List<Long> y = new ArrayList<>();
            for (String time : times) {
                int i = 0;
                for (ApiAuditVoRsp sdkLogTopVo : sdkLogTopVoList) {
                    if (sdkLogTopVo.getTime().equals(time)) {
                        y.add(sdkLogTopVo.getValue());
                        break;
                    }
                    i++;
                }
                if (i == sdkLogTopVoList.size()) {
                    y.add(0L);
                }
            }
            m.setVal(y);
            vertical.add(m);
        });

        return sdkLogGroupUrlVO;
    }


    /**
     * mybatis实现
     *
     * <p>
     * <p>
     * select url as name,count(*) as value,(ROUND(sum( case when status='1' then 1 else 0 end )*100/count(*),1)) as success
     * ,(ROUND(sum( case when status='0' then 1 else 0 end )*100/count(*),1)) as fail from t_sdk
     * WHERE invoke_time BETWEEN #{sdkLogDTO.from} AND #{sdkLogDTO.to}
     * <if test="sdkLogDTO.kgName != null">
     * and kg_name = #{sdkLogDTO.kgName}
     * </if>
     * <if test="sdkLogDTO.page != null">
     * and page = #{sdkLogDTO.page}
     * </if>
     * GROUP BY url ORDER BY
     * <choose>
     * <when test="order == 2">
     * success
     * </when>
     * <when test="order == 3">
     * fail
     * </when>
     * <otherwise>
     * value
     * </otherwise>
     * </choose>
     * DESC LIMIT 0,20
     *
     * @param req
     * @return
     */

    @Override
    public List<ApiAuditTopRsp> groupByTop(ApiAuditTopReq req) {
        setInvokeTime(req);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<ApiAudit> root = query.from(ApiAudit.class);
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        setApiQuery(expressions, root, cb, req);

        Path<String> urlPath = root.<String>get("url");
        Expression<Long> count = cb.count(urlPath);
        query.multiselect(urlPath.alias("name"), count.alias("value"));
        query.groupBy(urlPath).orderBy(cb.desc(count));
        query.where(predicate);

        List<Tuple> countTuple = entityManager.createQuery(query).getResultList();

        Map<String, Long> countMap = new LinkedHashMap<>();
        for (Tuple tuple : countTuple) {
            countMap.put((String) tuple.get(0), (Long) tuple.get(1));
        }

        expressions.add(cb.equal(root.<Integer>get("status"), 1));
        query.where(predicate);
        List<Tuple> successTuple = entityManager.createQuery(query).getResultList();
        Map<String, Long> successMap = new LinkedHashMap<>();
        for (Tuple tuple : successTuple) {
            successMap.put((String) tuple.get(0), (Long) tuple.get(1));
        }


        expressions.remove(expressions.size() - 1);
        expressions.add(cb.equal(root.<Integer>get("status"), 0));
        query.where(predicate);
        List<Tuple> failTuple = entityManager.createQuery(query).getResultList();
        Map<String, Long> failMap = new LinkedHashMap<>();
        for (Tuple tuple : failTuple) {
            failMap.put((String) tuple.get(0), (Long) tuple.get(1));
        }

        List<ApiAuditTopRsp> rsps = new ArrayList<>();

        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            ApiAuditTopRsp apiAuditTopRsp = new ApiAuditTopRsp();
            apiAuditTopRsp.setName(entry.getKey());
            Long c = entry.getValue();
            apiAuditTopRsp.setValue(c);
            Long success = successMap.get(entry.getKey());
            Long fail = failMap.get(entry.getKey());

            if (success != null) {
                BigDecimal divide = BigDecimal.valueOf(success).divide(BigDecimal.valueOf(c), 2, BigDecimal.ROUND_HALF_UP);
                apiAuditTopRsp.setSuccess(divide.doubleValue() * 100);
            } else {
                apiAuditTopRsp.setSuccess(0.0);
            }

            if (fail != null) {
                BigDecimal divide = BigDecimal.valueOf(fail).divide(BigDecimal.valueOf(c), 2, BigDecimal.ROUND_HALF_UP);
                apiAuditTopRsp.setFail(divide.doubleValue() * 100);
            } else {
                apiAuditTopRsp.setFail(0.0);
            }
            rsps.add(apiAuditTopRsp);
        }

        Integer order = req.getOrder();
        if (Objects.equals(order, 2)) {
            rsps.sort(Comparator.comparing(ApiAuditTopRsp::getSuccess, Comparator.reverseOrder()));
        } else if (Objects.equals(order, 3)) {
            rsps.sort(Comparator.comparing(ApiAuditTopRsp::getFail, Comparator.reverseOrder()));
        }
        return rsps.subList(0, rsps.size() > 20 ? 20 : rsps.size() - 1);
    }


    /**
     * mybatis实现
     * <p>
     * <p>
     * select page as name,count(*) as value from t_sdk where invoke_time BETWEEN #{from,jdbcType=TIMESTAMP} AND #{to,jdbcType=TIMESTAMP}
     * <if test="urls != null and urls.size() > 0">
     * and url in
     * <foreach collection="urls" item="item" index="index" open="(" close=")" separator=",">
     * #{item}
     * </foreach>
     * </if>
     * <if test="url != null">
     * and url = #{url}
     * </if>
     * <if test="kgName != null">
     * and kg_name = #{kgName}
     * </if>
     * <if test="page != null">
     * and page = #{page}
     * </if>
     * group by page order by value DESC
     */
    @Override
    public List<ApiAuditRsp> groupByPage(ApiAuditReq req) {
        setInvokeTime(req);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<ApiAudit> root = query.from(ApiAudit.class);
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();
        setApiQuery(expressions, root, cb, req);

        Selection<String> name = root.<String>get("page").alias("name");
        Selection<Long> value = cb.count(root.<String>get("page")).alias("value");
        query.multiselect(name, value);
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(cb.count(root.<String>get("page"))));
        query.groupBy(root.<String>get("page")).orderBy(orderList);
        query.where(predicate);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        List<Tuple> resultList = typedQuery.getResultList();
        List<ApiAuditRsp> auditRspList = new ArrayList<>();
        for (Tuple tuple : resultList) {
            ApiAuditRsp apiAuditRsp = new ApiAuditRsp();
            apiAuditRsp.setName((String) tuple.get(0));
            apiAuditRsp.setValue((Long) tuple.get(1));
            auditRspList.add(apiAuditRsp);
        }
        return auditRspList;
    }

    private void setApiQuery(List<Expression<Boolean>> expressions, Root<ApiAudit> root, CriteriaBuilder cb, ApiAuditReq req) {
        expressions.add(cb.between(root.<Date>get("invokeAt"), req.getBeginTime(), req.getEndTime()));
        if (req.getUrls() != null && !req.getUrls().isEmpty()) {
            expressions.add(cb.in(root.<String>get("url")).in(req.getUrls()));
        }
        if (StringUtils.hasText(req.getKgName())) {
            expressions.add(cb.equal(root.<String>get("kgName"), req.getKgName()));
        }
        if (StringUtils.hasText(req.getPage())) {
            expressions.add(cb.equal(root.<String>get("page"), req.getPage()));
        }
    }
}
