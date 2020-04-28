package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.DWFileRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author EYE
 */
public interface EntityFileRelationService {

    BasePage<DWFileRsp> listRelation(String kgName, EntityFileRelationQueryReq req);

    void createRelation(String kgName, EntityFileRelationReq req);

    void deleteRelation(List<String> idList);

    void deleteRelationByDwFileId(String dwFileId);

    void deleteById(String id);

    List<EntityFileRelationRsp> getRelationByDwFileId(String dwFileId);

    List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId);

    List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName,List<Long> entityIds);

    void addIndex(String kgName, Integer indexType, MultipartFile file);

    void updateIndex(String kgName, IndexRelationReq req);
}
