package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author EYE
 */
public interface EntityFileRelationService {

    Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req);

    void createRelation(String kgName, EntityFileRelationReq req);

    void updateRelation(EntityFileRelation req);

    void updateRelations(List<EntityFileRelation> relations);

    void deleteRelation(List<Integer> idList);

    void deleteRelationByDwFileId(Integer dwFileId);

    void deleteById(Integer id);

    List<EntityFileRelation> getRelationByDwFileId(Integer dwFileId);

    List<EntityFileRelation> getRelationByKgNameAndEntityId(String kgName,Long entityId);

    List<EntityFileRelation> getRelationByKgNameAndEntityIdIn(String kgName,List<Long> entityIds);

}
