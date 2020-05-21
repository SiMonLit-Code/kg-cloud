package com.plantdata.kgcloud.domain.file.service;

import com.plantdata.kgcloud.domain.file.entity.FileData;
import com.plantdata.kgcloud.domain.file.req.FileDataBatchReq;
import com.plantdata.kgcloud.domain.file.req.FileDataQueryReq;
import com.plantdata.kgcloud.domain.file.req.FileDataReq;
import com.plantdata.kgcloud.domain.file.req.FileDataUpdateReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDataRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 17:07
 */
public interface FileDataService {

    Page<FileDataRsp> getFileData(String userId, Long databaseId, Long tableId, FileDataQueryReq req);

    FileData fileAdd(FileDataReq req);

    void fileAddBatch(FileDataBatchReq fileTableReq, MultipartFile[] files);

    void fileUpdate(FileDataUpdateReq req);

    void fileDelete(String id);

    void fileDeleteBatch(List<String> ids);
}
