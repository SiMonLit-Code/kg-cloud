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

    void createRelation(String kgName, EntityFileRelationReq req);

    void updateRelation(EntityFileRelation req);

    void deleteRelation(List<Integer> idList);

    Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req);

    void deleteRelationByDwFileId(Integer dwFileId);

    void deleteRelationByMultiModalId(String multiModalId);

    EntityFileRelation getRelationByDwFileId(Integer dwFileId);

    EntityFileRelation getRelationByMultiModalId(String multiModalId);

}
