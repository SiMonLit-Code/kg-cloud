package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetFolderRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetFolderService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.FolderReq;
import com.plantdata.kgcloud.sdk.rsp.FolderRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 13:16
 **/
@Service
public class DataSetFolderServiceImpl implements DataSetFolderService {

    @Autowired
    private DataSetFolderRepository dataSetFolderRepository;
    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetFolder getDefaultFolder(String userId) {
        Optional<DataSetFolder> dataSetFolder = dataSetFolderRepository.findByUserIdAndDefaultedIsTrue(userId);
        return dataSetFolder.orElseGet(() -> {
            DataSetFolder folder = new DataSetFolder();
            folder.setId(kgKeyGenerator.getNextId());
            folder.setFolderName("默认文件夹");
            folder.setUserId(userId);
            folder.setDefaulted(true);
            return dataSetFolderRepository.save(folder);
        });
    }

    @Override
    public FolderRsp getDefault(String userId) {
        DataSetFolder defaultFolder = getDefaultFolder(userId);
        return ConvertUtils.convert(FolderRsp.class).apply(defaultFolder);
    }

    @Override
    public Set<Long> getFolderIds(String userId) {
        List<DataSetFolder> list = dataSetFolderRepository.findByUserId(userId);
        Set<Long> folderIds = new HashSet<>();
        for (DataSetFolder folder : list) {
            folderIds.add(folder.getId());
        }
        return folderIds;
    }

    @Override
    public Optional<DataSetFolder> getFolder(String userId, Long id) {
        return dataSetFolderRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<FolderRsp> findAll(String userId) {
        List<DataSetFolder> list = dataSetFolderRepository.findByUserId(userId);
        return list.stream()
                .map(ConvertUtils.convert(FolderRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FolderRsp folderInsert(String userId,FolderReq req) {
        DataSetFolder folder = new DataSetFolder();
        BeanUtils.copyProperties(req, folder);
        folder.setId(kgKeyGenerator.getNextId());
        folder.setDefaulted(false);
        folder.setUserId(userId);
        DataSetFolder save = dataSetFolderRepository.save(folder);
        return ConvertUtils.convert(FolderRsp.class).apply(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void folderDelete(String userId, Long id, Boolean deleteData) {
        DataSetFolder folder = dataSetFolderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.FOLDER_NOT_EXISTS));
        Boolean defaulted = folder.getDefaulted();
        if (Objects.equals(true, defaulted)) {
            throw BizException.of(KgmsErrorCodeEnum.FOLDER_DISABLE_DELETE);
        }
        dataSetFolderRepository.deleteByIdAndUserId(id, userId);
        List<DataSet> dataSetList = dataSetService.findByFolderId(folder.getId());
        if (!dataSetList.isEmpty()) {
            List<Long> ids = dataSetList.stream().map(DataSet::getId).collect(Collectors.toList());
            if (Objects.equals(true, deleteData)) {
                dataSetService.batchDelete(userId, ids);
            } else {
                dataSetService.move(userId, ids, null);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FolderRsp folderUpdate(String userId, Long id, FolderReq req) {
        DataSetFolder folder = dataSetFolderRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.FOLDER_NOT_EXISTS));
        BeanUtils.copyProperties(req, folder);
        DataSetFolder save = dataSetFolderRepository.save(folder);
        return ConvertUtils.convert(FolderRsp.class).apply(save);
    }
}
