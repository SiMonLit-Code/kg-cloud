package com.plantdata.kgcloud.domain.dw.service.impl;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.util.PatternUtils;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWFileTableRepository;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableBatchReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableUpdateReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataSearchReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWFileTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.TableDataService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 16:43
 **/
@Service
public class TableDataServiceImpl implements TableDataService {

    @Autowired
    private DWService dwService;

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private FastdfsTemplate fastdfsTemplate;

    @Autowired
    private DWFileTableRepository fileTableRepository;

    @Override
    public Page<Map<String, Object>> getData(String userId, Long datasetId, Long tableId, DataOptQueryReq baseReq) {

        Map<String, Object> query = new HashMap<>();
        if (StringUtils.hasText(baseReq.getField()) && StringUtils.hasText(baseReq.getKw())) {
            Map<String, String> value = new HashMap<>();
            value.put(baseReq.getField(), baseReq.getKw());
            query.put("search", value);
        }

        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
            List<Map<String, Object>> maps = provider.find(baseReq.getOffset(), baseReq.getLimit(), query);
            long count = provider.count(query);
            return new PageImpl<>(maps, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
        }
    }

    private DataOptProvider getProvider(String userId, Long datasetId, Long tableId, MongoProperties mongoProperties) {

        DWDatabaseRsp database = dwService.getDetail(datasetId);

        if (database == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        DWTable table = dwService.getTableDetail(tableId);
        if (table == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DataOptConnect connect = DataOptConnect.of(database, table, mongoProperties);
        return DataOptProviderFactory.createProvider(connect);
    }

    @Override
    public List<Map<String, Object>> statistic(String userId, Long datasetId, Long tableId, DwTableDataStatisticReq statisticReq) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            return provider.aggregateStatistics(statisticReq.getFilterMap(), statisticReq.getGroupMap(), statisticReq.getSortMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> search(String userId, Long datasetId, Long tableId, DwTableDataSearchReq searchReq) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            List<String> fields = CollectionUtils.isEmpty(searchReq.getFields()) ? searchReq.getFields() : provider.getFields();
            Map<String, Object> queryMap = Maps.newHashMap();
            List<Map<String, Object>> collect = fields.stream().map(a -> {
                Map<String, Object> map = Maps.newHashMapWithExpectedSize(1);
                map.put(a, PatternUtils.getLikeStr(a));
                return map;
            }).collect(Collectors.toList());
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("$or", collect);
            queryMap.put("search", map);
            return provider.find(searchReq.getOffset(), searchReq.getLimit(), queryMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getDataById(String userId, Long datasetId, Long tableId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            Map<String, Object> one = provider.findOne(dataId);
            DWTable table = dwService.getTableDetail(tableId);
            if (table == null) {
                throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
            }
            List<DataSetSchema> schema = table.getSchema();
            Map<String, DataSetSchema> schemaMap = new HashMap<>();
            for (DataSetSchema o : schema) {
                schemaMap.put(o.getField(), o);
            }
            Map<String, Object> result = new HashMap<>();

            for (Map.Entry<String, Object> entry : one.entrySet()) {
                DataSetSchema scm = schemaMap.get(entry.getKey());
                if (scm != null) {
                    if (Objects.equals(scm.getType(), FieldType.DOUBLE.getCode()) ||
                            Objects.equals(scm.getType(), FieldType.FLOAT.getCode())) {
                        BigDecimal value = new BigDecimal(entry.getValue().toString());
                        if (value.compareTo(new BigDecimal(value.intValue())) == 0) {
                            DecimalFormat f = new DecimalFormat("##.0");
                            result.put(entry.getKey(), f.format(value));
                        } else {
                            result.put(entry.getKey(), value);
                        }
                    } else {
                        result.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void fileAdd(DWFileTableReq req) {

        byte[] bytes = fastdfsTemplate.downloadFile(req.getPath());

        DWFileTable fileTable = ConvertUtils.convert(DWFileTable.class).apply(req);
        fileTable.setFileSize(new Long(bytes.length));
        fileTable.setUserId(SessionHolder.getUserId());
        if (req.getFileName() != null && req.getFileName().contains(".")) {
            fileTable.setType(req.getFileName().substring(req.getFileName().lastIndexOf(".") + 1));
        }
        fileTable.setDataBaseId(req.getDataBaseId());

        fileTableRepository.save(fileTable);
    }

    @Override
    public Page<DWFileTableRsp> getFileData(String userId, Long databaseId, Long tableId, DataOptQueryReq baseReq) {

        PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), Sort.by(Sort.Order.desc("createAt")));

        Specification<DWFileTable> specification = new Specification<DWFileTable>() {
            @Override
            public Predicate toPredicate(Root<DWFileTable> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!com.alibaba.excel.util.StringUtils.isEmpty(baseReq.getKw())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + baseReq.getKw() + "%");
                    predicates.add(likename);
                }

                Predicate databaseIdPre = criteriaBuilder.equal(root.get("dataBaseId").as(Long.class), databaseId);
                predicates.add(databaseIdPre);

                Predicate tableIdPre = criteriaBuilder.equal(root.get("tableId").as(Long.class), tableId);
                predicates.add(tableIdPre);

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWFileTable> all = fileTableRepository.findAll(specification, pageable);

        Page<DWFileTableRsp> map = all.map(ConvertUtils.convert(DWFileTableRsp.class));

        return map;
    }

    @Override
    public void fileUpdate(DWFileTableUpdateReq fileTableReq) {

        Optional<DWFileTable> opt = fileTableRepository.findById(fileTableReq.getId());

        if (opt.isPresent()) {

            DWFileTable fileTable = opt.get();
            fileTable.setName(fileTableReq.getName());
            fileTable.setOwner(fileTableReq.getOwner());
            fileTable.setKeyword(fileTableReq.getKeyword());
            fileTable.setDescription(fileTableReq.getDescription());
            fileTableRepository.save(fileTable);
        }

    }

    @Override
    public void fileDelete(Integer id) {
        fileTableRepository.deleteById(id);
    }

    @Override
    public void fileAddBatch(DWFileTableBatchReq fileTableReq, MultipartFile[] files) {

        if (files == null || files.length == 0) {
            return;
        }

        for (MultipartFile file : files) {
            DWFileTable fileTable = ConvertUtils.convert(DWFileTable.class).apply(fileTableReq);
            fileTable.setFileSize(file.getSize());
            fileTable.setUserId(SessionHolder.getUserId());
            if (file.getOriginalFilename().contains(".")) {
                fileTable.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                fileTable.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            } else {
                fileTable.setName(file.getOriginalFilename());
            }
            fileTable.setDataBaseId(fileTableReq.getDataBaseId());
            fileTable.setPath(fastdfsTemplate.uploadFile(file).getFullPath());

            fileTableRepository.save(fileTable);
        }

        return;
    }
}
