package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.entity.MultiModal;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lp
 */
public interface EntityFileRelationService {

    Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req);

    void createRelation(String kgName, EntityFileRelationReq req);

    void deleteRelationByFileId(String fileId);

    void deleteRelationByFileIds(List<String> fileIds);

    void deleteIndexById(String kgName, String id);

    void deleteIndexByIds(String kgName, List<String> idList);

    void deleteByEntityIds(String kgName, List<Long> entityIds);

    MultiModal getMultiModalById(String kgName, String id);

    List<EntityFileRelation> getRelationByDwFileId(String kgName, String fileId);

    List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId);

    List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds, Integer type);

    void addIndex(String kgName, Integer indexType, MultipartFile file);

    void updateIndex(String kgName, IndexRelationReq req);

    void cancelIndex(String kgName, IndexRelationReq req);

    void addFile(String kgName, Long databaseId, Long tableId);

    boolean checkExist(String kgName, Long entityId, String fileId);

    boolean checkSize(String kgName, Long entityId);

}
