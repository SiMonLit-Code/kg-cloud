package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildModelRepository;
import com.plantdata.kgcloud.domain.dw.req.StandardSearchReq;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateRsp;
import com.plantdata.kgcloud.domain.dw.service.StandardTemplateService;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StandardTemplateServiceImpl implements StandardTemplateService {

    @Autowired
    private DWPrebuildModelRepository modelRepository;

    private final Function<DWPrebuildModel, StandardTemplateRsp> st2rsp = (s) -> {
        StandardTemplateRsp stRsp = new StandardTemplateRsp();
        BeanUtils.copyProperties(s, stRsp);
        return stRsp;
    };

    @Override
    public List<StandardTemplateRsp> findAll(StandardSearchReq req) {

        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());

        Specification<DWPrebuildModel> specification = new Specification<DWPrebuildModel>() {
            @Override
            public Predicate toPredicate(Root<DWPrebuildModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(req.getKw())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getKw() + "%");
                    predicates.add(likename);
                }


                if (!StringUtils.isEmpty(req.getModelType())) {

                    Predicate modelTypeEq = criteriaBuilder.equal(root.get("modelType").as(String.class), req.getModelType());
                    predicates.add(modelTypeEq);
                }

                //查询管理员发布公开的或者自己发布的
                Predicate isPublic = criteriaBuilder.equal(root.get("permission").as(Integer.class), 1);
                predicates.add(isPublic);

                //只能查询发布过的
                predicates.add(criteriaBuilder.equal(root.get("status").as(String.class),"1"));

                predicates.add(criteriaBuilder.equal(root.get("isStandardTemplate").as(String.class),"1"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWPrebuildModel> all = modelRepository.findAll(specification, pageable);

        return all.stream().map(st2rsp).collect(Collectors.toList());
    }

    @Override
    public StandardTemplateRsp findOne(String userId, Integer standardTemplateId) {

        Optional<DWPrebuildModel> byId = modelRepository.findById(standardTemplateId);

        if(!byId.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.STANDARD_TEMPLATE_NOT_EXIST);
        }

        return st2rsp.apply(byId.get());
    }
}
