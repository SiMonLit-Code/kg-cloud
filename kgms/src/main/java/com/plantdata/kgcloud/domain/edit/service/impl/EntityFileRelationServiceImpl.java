package com.plantdata.kgcloud.domain.edit.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.repository.EntityFileRelationRepository;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EYE
 */
@Service
public class EntityFileRelationServiceImpl implements EntityFileRelationService {

    @Autowired
    private EntityFileRelationRepository entityFileRelationRepository;
    @Autowired
    private BasicInfoService basicInfoService;

    @Override
    public void createRelation(String kgName, EntityFileRelationReq req) {
        EntityFileRelation relation = new EntityFileRelation();
        BeanUtils.copyProperties(req, relation);
        relation.setKgName(kgName);
        relation.setCreateAt(new Date());
        entityFileRelationRepository.save(relation);
    }

    @Override
    public void updateRelation(EntityFileRelation req) {
        entityFileRelationRepository.save(req);
    }

    @Override
    public void deleteRelation(List<Integer> idList) {
        for (Integer id : idList) {
            entityFileRelationRepository.deleteById(id);
        }
    }

    @Override
    public Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        Specification<EntityFileRelation> specification = new Specification<EntityFileRelation>() {
            @Override
            public Predicate toPredicate(Root<EntityFileRelation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                Predicate predicate = criteriaBuilder.equal(root.get("kgName").as(String.class), kgName);
                predicates.add(predicate);

                if (req.getName() != null) {
                    Predicate name = criteriaBuilder.like(root.get("name").as(String.class), "%" + req.getName() + "%");
                    predicates.add(name);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
        Page<EntityFileRelation> list = entityFileRelationRepository.findAll(specification, pageable);
        Page<EntityFileRelationRsp> rspList = list.map(ConvertUtils.convert(EntityFileRelationRsp.class));
        List<Long> entityIdList = list.get().map(EntityFileRelation::getEntityId).collect(Collectors.toList());
        List<BasicInfoRsp> basicInfoRsps = basicInfoService.listByIds(kgName, entityIdList);
        for (EntityFileRelationRsp entityFileRelationRsp : rspList) {
            List<BasicInfoRsp> collect = basicInfoRsps.stream()
                    .filter(entity -> entity.getId().equals(entityFileRelationRsp.getEntityId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                entityFileRelationRsp.setEntityName(collect.get(0).getName());
            }
        }
        return rspList;
    }

    @Override
    @Transactional
    public void deleteRelationByDwFileId(Integer dwFileId) {
        entityFileRelationRepository.deleteRelationByDwFileId(dwFileId);
    }

    @Override
    @Transactional
    public void deleteRelationByMultiModalId(String multiModalId) {
        entityFileRelationRepository.deleteRelationByMultiModalId(multiModalId);
    }

    @Override
    public EntityFileRelation getRelationByDwFileId(Integer dwFileId) {
        return entityFileRelationRepository.getRelationByDwFileId(dwFileId);
    }

    @Override
    public EntityFileRelation getRelationByMultiModalId(String multiModalId) {
        return entityFileRelationRepository.getRelationByMultiModalId(multiModalId);
    }
}
