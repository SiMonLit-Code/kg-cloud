package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.config.EsProperties;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetCreateReq;
import com.plantdata.kgcloud.sdk.req.DataSetPageReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DataSetUpdateReq;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:40
 **/
@Slf4j
@Service
public class DataSetServiceImpl implements DataSetService {

    private final static String DATA_PREFIX = "dataset";
    private final static String JOIN = "_";

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private EsProperties esProperties;

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    private DataSetFolderService dataSetFolderService;


    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    private String genDataName(String userId, String key) {
        return userId + JOIN + DATA_PREFIX + JOIN + key;
    }

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
        return dataSetRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }

    @Override
    public DataSet findOne(Long id) {
        return dataSetRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }


    @Override
    public DataSetRsp findById(String userId, Long id) {
        Optional<DataSet> one = dataSetRepository.findByUserIdAndId(userId, id);
        return one.map(ConvertUtils.convert(DataSetRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, Long id) {
        DataSet dataSet = findOne(userId, id);
        DataOptConnect connect = DataOptConnect.of(dataSet);
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, dataSet.getDataType())) {
            provider.dropTable();
        } catch (IOException e) {
            log.error("delete fail...", e);
        }
        dataSetRepository.deleteByUserIdAndId(userId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String userId, Collection<Long> ids) {
        for (Long id : ids) {
            delete(userId, id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp insert(String userId, DataSetCreateReq req) {
        DataSet target = new DataSet();
        BeanUtils.copyProperties(req, target);
        Set<Long> folderIds = dataSetFolderService.getFolderIds(userId);
        if (!folderIds.contains(target.getFolderId())) {
            DataSetFolder folder = dataSetFolderService.getDefaultFolder(userId);
            target.setFolderId(folder.getId());
        }
        String dataName = genDataName(userId, req.getKey());
        Optional<DataSet> dataSet = dataSetRepository.findByDataName(dataName);
        if (dataSet.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_KEY_EXISTS);
        }

        target.setId(kgKeyGenerator.getNextId());
        target.setDataName(dataName);
        DataType type = DataType.findType(req.getDataType());
        target.setDataType(type);
        target.setDbName(userId + JOIN + DATA_PREFIX);
        if (type == DataType.MONGO) {
            target.setAddr(Arrays.asList(mongoProperties.getAddrs()));
            target.setTbName(req.getKey());
            /*
            target.setPassword(mongoProperties.getPassword());
            target.setUsername(mongoProperties.getUsername());
            */
        } else if (type == DataType.ELASTIC) {
            target.setAddr(esProperties.getAddrs());
            target.setDbName(dataName);
            target.setTbName("_doc");
        }
        target.setEditable(true);
        target.setPrivately(true);
        List<DataSetSchema> schema = req.getSchema();
        List<String> fields = new ArrayList<>();
        for (DataSetSchema dataSetSchema : schema) {
            fields.add(dataSetSchema.getField());
        }
        target.setFields(fields);


        DataOptConnect dataOptConnect = DataOptConnect.of(target);
        DataOptProvider provider = DataOptProviderFactory.createProvider(dataOptConnect, type);
        provider.createTable(schema);

        target = dataSetRepository.save(target);
        return ConvertUtils.convert(DataSetRsp.class).apply(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp update(String userId, Long id, DataSetUpdateReq req) {
        Optional<DataSet> one = dataSetRepository.findByUserIdAndId(userId,id);
        DataSet target = one.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
        BeanUtils.copyProperties(req, target);
        target = dataSetRepository.save(target);
        return ConvertUtils.convert(DataSetRsp.class).apply(target);

    }

    @Override
    public List<DataSetSchema> resolve(Integer dataType, MultipartFile file) {
        return null;
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
