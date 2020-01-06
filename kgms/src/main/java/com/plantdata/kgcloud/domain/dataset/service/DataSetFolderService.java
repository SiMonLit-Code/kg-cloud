package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import com.plantdata.kgcloud.sdk.req.FolderReq;
import com.plantdata.kgcloud.sdk.rsp.FolderRsp;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 13:15
 **/
public interface DataSetFolderService {

    /**
     * 获取默认文件夹
     *
     * @return
     */
    DataSetFolder getDefaultFolder(String userId);

    /**
     * @param userId
     * @return
     */
    FolderRsp getDefault(String userId);

    /**
     * 获取所有文件夹Id
     *
     * @return
     */
    Set<Long> getFolderIds(String userId);

    /**
     * 根据id查找文件夹
     *
     * @param id
     * @return
     */
    Optional<DataSetFolder> getFolder(String userId, Long id);

    /**
     * 查询所有
     *
     * @return
     */
    List<FolderRsp> findAll(String userId);

    /**
     * 新建
     *
     * @param req
     * @return
     */
    FolderRsp folderInsert(FolderReq req);

    /**
     * 删除
     *
     * @param id
     * @param deleteData
     * @return
     */
    void folderDelete(String userId, Long id, Boolean deleteData);

    /**
     * 修改
     *
     * @param id
     * @param req
     * @return
     */
    FolderRsp folderUpdate(String userId, Long id, FolderReq req);

}
