package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetReq;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:40
 **/
@Slf4j
@Service
public class DataSetServiceImpl implements DataSetService {

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    private DataSetFolderService dataSetFolderService;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    public List<DataSetRsp> findAll(String userId) {
        DataSet probe = DataSet.builder()
                .userId(SessionHolder.getUserId())
                .build();
        List<DataSet> all = dataSetRepository.findAll(Example.of(probe));
        return all.stream()
                .map(ConvertUtils.convert(DataSetRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public Page<DataSetRsp> findAll(String userId, BaseReq baseReq) {
        DataSet probe = DataSet.builder()
                .userId(userId)
                .build();
        Page<DataSet> all = dataSetRepository.findAll(Example.of(probe), PageRequest.of(baseReq.getPage() - 1,
                baseReq.getSize()));
        return all.map(ConvertUtils.convert(DataSetRsp.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<DataSetRsp> findAll(String userId, DataSetPageReq req) {
        DataSet.DataSetBuilder dataSetBuilder = DataSet.builder()
                .userId(userId);
        Long folderId = req.getFolderId();
        if (folderId != null) {
            dataSetBuilder.folderId(folderId);
        }
        DataSet probe = dataSetBuilder.build();
        Page<DataSet> all = dataSetRepository.findAll(Example.of(probe), PageRequest.of(req.getPage() - 1, req.getSize()));
        DataSetFolder folder = dataSetFolderService.getDefaultFolder(userId);
        Set<Long> folderIds = dataSetFolderService.getFolderIds(userId);
        for (DataSet dataSet : all.getContent()) {
            if (!folderIds.contains(dataSet.getFolderId())) {
                dataSet.setFolderId(folder.getId());
                dataSetRepository.save(dataSet);
            }
        }
        return all.map(ConvertUtils.convert(DataSetRsp.class));
    }

    @Override
    public List<DataSet> findByFolderId(Long folderId) {
        return dataSetRepository.findByFolderId(folderId);
    }

    @Override
    public DataSet findOne(String userId, Long id) {
        return dataSetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }

    @Override
    public DataSet findOne(Long id) {
        return dataSetRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }


    @Override
    public DataSetRsp findById(String userId, Long id) {
        Optional<DataSet> one = dataSetRepository.findByIdAndUserId(id, userId);
        return one.map(ConvertUtils.convert(DataSetRsp.class)).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, Long id) {
        DataSet dataSet = dataSetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
        DataOptConnect connect = DataOptConnect.builder()
                .addresses(dataSet.getAddr())
                .username(dataSet.getUsername())
                .password(dataSet.getPassword())
                .build();
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, dataSet.getDataType())) {
            provider.dropTable();
        } catch (IOException e) {
            log.error("delete fail...", e);
        }
        dataSetRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public DataSetRsp insert(DataSetReq dataSetReq) {
        return null;
    }

    @Override
    public void batchDelete(String userId, Collection<Long> ids) {
        for (Long id : ids) {
            delete(userId, id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp insert(String userId, DataSetReq req) {
        DataSet target = new DataSet();
        BeanUtils.copyProperties(req, target);
        Set<Long> folderIds = dataSetFolderService.getFolderIds(userId);
        if (!folderIds.contains(target.getFolderId())) {
            DataSetFolder folder = dataSetFolderService.getDefaultFolder(userId);
            target.setFolderId(folder.getId());
        }
        target.setId(kgKeyGenerator.getNextId());
        target = dataSetRepository.save(target);
        return ConvertUtils.convert(DataSetRsp.class).apply(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp update(String userId, Long id, DataSetReq req) {
        Optional<DataSet> one = dataSetRepository.findByIdAndUserId(id, userId);
        if (one.isPresent()) {
            DataSet target = one.get();
            BeanUtils.copyProperties(req, target);
            target = dataSetRepository.save(target);
            return ConvertUtils.convert(DataSetRsp.class).apply(target);
        }
        throw BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS);
    }

    @Override
    public void move(String userId, Collection<Long> ids, Long folderId) {
        if (folderId == null) {
            folderId = dataSetFolderService.getDefaultFolder(userId).getId();
        } else {
            dataSetFolderService.getFolder(userId, folderId).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.FOLDER_NOT_EXISTS));
        }
        List<DataSet> dataSetList = dataSetRepository.findAllById(ids);
        for (DataSet dataSet : dataSetList) {
            dataSet.setFolderId(folderId);
        }
        dataSetRepository.saveAll(dataSetList);
    }

}
