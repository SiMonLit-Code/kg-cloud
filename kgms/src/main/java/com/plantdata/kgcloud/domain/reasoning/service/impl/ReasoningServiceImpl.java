package com.plantdata.kgcloud.domain.reasoning.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.prebuilder.util.SortUtil;
import com.plantdata.kgcloud.domain.reasoning.entity.Reasoning;
import com.plantdata.kgcloud.domain.reasoning.repository.ReasoningRepository;
import com.plantdata.kgcloud.domain.reasoning.req.*;
import com.plantdata.kgcloud.domain.reasoning.rsp.ReasoningRsp;
import com.plantdata.kgcloud.domain.reasoning.service.ReasoningService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 17:00
 **/
@Service
public class ReasoningServiceImpl implements ReasoningService {

    @Autowired
    private ReasoningRepository reasoningRepository;

    @Override
    public Page<ReasoningRsp> list(String userId, ReasoningQueryReq reasoningQueryReq) {

        PageRequest pageable = PageRequest.of(reasoningQueryReq.getPage() - 1, reasoningQueryReq.getSize(), SortUtil.buildSort(reasoningQueryReq.getSorts()));

        Specification<Reasoning> specification = new Specification<Reasoning>() {
            @Override
            public Predicate toPredicate(Root<Reasoning> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(reasoningQueryReq.getName())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + reasoningQueryReq.getName() + "%");
                    predicates.add(likename);
                }

                predicates.add(criteriaBuilder.equal(root.get("kgName").as(String.class), reasoningQueryReq.getKgName()));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<Reasoning> all = reasoningRepository.findAll(specification, pageable);

        Page<ReasoningRsp> map = all.map(ConvertUtils.convert(ReasoningRsp.class));

        return map;
    }

    @Override
    public ReasoningRsp add(String userId, ReasoningAddReq reasoningAddReq) {

        Reasoning reasoning = ConvertUtils.convert(Reasoning.class).apply(reasoningAddReq);

        reasoning.setUserId(SessionHolder.getUserId());

        reasoningRepository.save(reasoning);
        return ConvertUtils.convert(ReasoningRsp.class).apply(reasoning);
    }

    @Override
    public ReasoningRsp update(String userId, ReasoningUpdateReq reasoningUpdateReq) {

        Optional<Reasoning> optional = reasoningRepository.findById(reasoningUpdateReq.getId());

        Reasoning reasoning = optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));

        reasoning.setConfig(reasoningUpdateReq.getConfig());
        reasoning.setName(reasoningUpdateReq.getName());

        reasoningRepository.save(reasoning);
        return ConvertUtils.convert(ReasoningRsp.class).apply(reasoning);
    }

    @Override
    public void delete(String userId, Integer id) {
        reasoningRepository.deleteById(id);
    }

    @Override
    public CommonBasicGraphExploreRsp verification(String userId, ReasoningVerifyReq reasoningVerifyReq) {
        return null;
    }

    @Override
    public CommonBasicGraphExploreRsp execute(String userId, ReasoningExecuteReq reasoningExecuteReq) {
        return null;
    }
}
