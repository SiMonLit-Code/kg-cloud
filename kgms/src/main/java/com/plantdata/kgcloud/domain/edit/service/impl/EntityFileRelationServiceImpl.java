package com.plantdata.kgcloud.domain.edit.service.impl;

import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.repository.EntityFileRelationRepository;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        Page<EntityFileRelation> list = entityFileRelationRepository.getByKgNameAndNameContaining(kgName, req.getName(), pageable);
        Page<EntityFileRelationRsp> rspList = list.map(ConvertUtils.convert(EntityFileRelationRsp.class));

        List<Long> entityIdList = list.get().map(EntityFileRelation::getEntityId).collect(Collectors.toList());

        Map<Long, String> nameMap = basicInfoService.listByIds(kgName, entityIdList)
                .stream().collect(Collectors.toMap(BasicInfoRsp::getId, BasicInfoRsp::getName, (k1, k2) -> k1));

        for (EntityFileRelationRsp relation : rspList) {
            relation.setEntityName(nameMap.get(relation.getEntityId()));
        }
        return rspList;
    }

    @Override
    public void createRelation(String kgName, EntityFileRelationReq req) {
        EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
        relation.setKgName(kgName);
        relation.setCreateAt(new Date());
        entityFileRelationRepository.save(relation);
    }

    @Override
    public void updateRelation(EntityFileRelation req) {
        entityFileRelationRepository.save(req);
    }

    @Override
    public void updateRelations(List<EntityFileRelation> relations) {
        entityFileRelationRepository.saveAll(relations);
    }

    @Override
    @Transactional
    public void deleteRelation(List<Integer> idList) {
        List<EntityFileRelation> list = entityFileRelationRepository.findAllById(idList);
        for (EntityFileRelation relation : list) {
            entityFileRelationRepository.deleteByDwFileId(relation.getDwFileId());
        }
    }

    @Override
    @Transactional
    public void deleteRelationByDwFileId(Integer dwFileId) {
        entityFileRelationRepository.deleteByDwFileId(dwFileId);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        entityFileRelationRepository.deleteById(id);
    }

    @Override
    public List<EntityFileRelation> getRelationByDwFileId(Integer dwFileId) {
        return entityFileRelationRepository.getByDwFileId(dwFileId);
    }

    @Override
    public List<EntityFileRelation> getRelationByKgNameAndEntityId(String kgName, Long entityId) {
        return entityFileRelationRepository.getByKgNameAndEntityId(kgName, entityId);
    }

    @Override
    public List<EntityFileRelation> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds) {
        return entityFileRelationRepository.getByKgNameAndEntityIdIn(kgName, entityIds);
    }

}
