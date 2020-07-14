package ai.plantdata.kgcloud.domain.file.service;

import ai.plantdata.kgcloud.domain.file.entity.FileData;
import ai.plantdata.kgcloud.domain.file.req.FileDataBatchReq;
import ai.plantdata.kgcloud.domain.file.req.FileDataQueryReq;
import ai.plantdata.kgcloud.domain.file.req.FileDataReq;
import ai.plantdata.kgcloud.domain.file.req.FileDataUpdateReq;
import ai.plantdata.kgcloud.domain.file.rsq.FileDataRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 17:07
 */
public interface FileDataService {

    Page<FileDataRsp> listFileData(Long fileSystemId, Long folderId, FileDataQueryReq req);

    FileData get(String id);

    FileData fileAdd(FileDataReq req);

    void fileAddBatch(FileDataBatchReq fileTableReq, MultipartFile[] files);

    void fileUpdate(FileDataUpdateReq req);

    void update(FileData fileData);

    void fileDelete(String id);

    void fileDeleteBatch(List<String> ids);

    void fileDeleteByFolderId(Long folderId);
}
