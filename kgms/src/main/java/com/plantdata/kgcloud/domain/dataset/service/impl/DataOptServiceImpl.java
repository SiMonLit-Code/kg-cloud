package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DataSetSmokeReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetAddReq;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.smoke.core.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 18:43
 **/
@Service
public class DataOptServiceImpl implements DataOptService {

    @Autowired
    private DataSetService dataSetService;

    @Override
    public DataOptProvider getProvider(String userId, Long datasetId) {
        DataSet one = dataSetService.findOne(userId, datasetId);
        DataOptConnect connect = DataOptConnect.of(one);
        return DataOptProviderFactory.createProvider(connect, one.getDataType());
    }

    @Override
    public Page<Map<String, Object>> getData(String userId, Long datasetId, DataOptQueryReq req) {
        Map<String, Object> query = new HashMap<>();
        if (StringUtils.hasText(req.getField()) && StringUtils.hasText(req.getKw())) {
            Map<String, String> value = new HashMap<>();
            value.put(req.getField(), req.getKw());
            query.put("search", value);
        }
        if (req.getResultType() != null) {
            query.put("resultType", req.getResultType());
        }
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
            List<Map<String, Object>> maps = provider.find(req.getOffset(), req.getLimit(), query);
            long count = provider.count(query);
            return new PageImpl<>(maps, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> getDataById(String userId, Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            Map<String, Object> one = provider.findOne(dataId);
            DataSet dataSet = dataSetService.findOne(userId, datasetId);
            List<DataSetSchema> schema = dataSet.getSchema();
            Map<String, DataSetSchema> schemaMap = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            for (DataSetSchema o : schema) {
                schemaMap.put(o.getField(), o);
            }
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
    public Map<String, Object> insertData(String userId, Long datasetId, Map<String, Object> data) {
        data.remove("_id");
        DataSet one = dataSetService.findOne(userId, datasetId);
        List<DataSetSchema> schema = one.getSchema();
        Map<String, Object> result = validate(schema, data);
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            result.put(DataConst.CREATE_AT, DateUtils.formatDatetime());
            result.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
            return provider.insert(result);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void upload(String userId, Long datasetId, MultipartFile file) throws Exception {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            String filename = file.getOriginalFilename();
            if (filename != null) {
                int i = filename.lastIndexOf(".");
                String extName = filename.substring(i);
                if (KgmsConstants.FileType.XLSX.equalsIgnoreCase(extName) || KgmsConstants.FileType.XLS.equalsIgnoreCase(extName)) {
                    excelFileHandle(provider, file);
                } else if (KgmsConstants.FileType.JSON.equalsIgnoreCase(extName)) {
                    jsonFileHandle(provider, file);
                }
            }
        }
    }

    private void excelFileHandle(DataOptProvider provider, MultipartFile file) throws Exception {
        EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, Object>>() {
            Map<Integer, String> head;
            List<Map<String, Object>> mapList = new ArrayList<>();

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                head = headMap;
            }

            @Override
            public void invoke(Map<Integer, Object> data, AnalysisContext context) {
                Map<String, Object> map = new HashMap<>(data.size());
                for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                    map.put(head.get(entry.getKey()), entry.getValue());
                }
                map.remove("_id");
                map.put(DataConst.CREATE_AT, DateUtils.formatDatetime());
                map.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
                mapList.add(map);
                if (mapList.size() == 10000) {
                    provider.batchInsert(mapList);
                    mapList.clear();
                }
                if (!mapList.isEmpty()) {
                    provider.batchInsert(mapList);
                    mapList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet().doRead();
    }

    private void jsonFileHandle(DataOptProvider provider, MultipartFile file) throws Exception {
        List<Map<String, Object>> dataList = JacksonUtils.readValue(file.getInputStream(), new TypeReference<List<Map<String, Object>>>() {
        });
        provider.batchInsert(dataList);
    }

    @Override
    public Map<String, Object> updateData(String userId, Long datasetId, String dataId, Map<String, Object> data) {
        if (data != null && !data.isEmpty()) {
            DataSet one = dataSetService.findOne(userId, datasetId);
            List<DataSetSchema> schema = one.getSchema();
            Map<String, Object> result = validate(schema, data);
            try (DataOptProvider provider = getProvider(userId, datasetId)) {
                result.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
                return provider.update(dataId, result);
            } catch (IOException e) {
                throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
            }
        } else {
            return data;
        }
    }

    private Map<String, Object> validate(List<DataSetSchema> schema, Map<String, Object> data) {
        Map<String, DataSetSchema> schemaMap = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        for (DataSetSchema o : schema) {
            schemaMap.put(o.getField(), o);
        }
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            DataSetSchema dataSetSchema = schemaMap.get(key);
            if (dataSetSchema != null) {
                FieldType code = FieldType.findCode(dataSetSchema.getType());
                try {
                    Object format = fieldFormat(value, code);
                    if (format != null) {
                        result.put(key, format);
                    }
                } catch (Exception e) {
                    throw BizException.of(KgmsErrorCodeEnum.DATASET_FIELD_ERROR);
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private Object fieldFormat(Object o, FieldType field) throws Exception {
        return field.deserialize(o);
    }

    @Override
    public void batchInsertData(String userId, Long datasetId, List<Map<String, Object>> dataList) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            provider.batchInsert(dataList);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void deleteData(String userId, Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            provider.delete(dataId);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void deleteAll(String userId, Long datasetId) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            provider.deleteAll();
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void batchDeleteData(String userId, Long datasetId, Collection<String> dataIds) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            provider.batchDelete(dataIds);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void exportData(String userId, Long datasetId, HttpServletResponse response) throws Exception {
        DataSet one = dataSetService.findOne(userId, datasetId);
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            List<Map<String, Object>> mapList = provider.find(0, 10000, null);
            List<List<Object>> resultList = new ArrayList<>();
            List<DataSetSchema> schema = one.getSchema();
            for (Map<String, Object> objectMap : mapList) {
                List<Object> objects = new ArrayList<>(schema.size());
                for (DataSetSchema dataSetSchema : schema) {
                    String field = dataSetSchema.getField();
                    objects.add(objectMap.get(field));
                }
                resultList.add(objects);
            }
            response.setContentType("application/octet-stream");
            String dataName = one.getDataName() + "_" + System.currentTimeMillis() + ".xlsx";
            String fileName = URLEncoder.encode(dataName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream).head(head(schema)).sheet().doWrite(resultList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddDataForDataSet(String userId, DataSetAddReq addReq) {
        List<Long> ids = dataSetService.findByDataNames(userId, Lists.newArrayList(addReq.getDataName()));
        if (CollectionUtils.isEmpty(ids)) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS);
        }
        this.batchInsertData(userId, ids.get(0), addReq.getDataList());
    }

    @Override
    public List<Map<String, Long>> statistics(String userId, Long datasetId) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            return provider.statistics();
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> smoke(String userId, DataSetSmokeReq req) {
        return Check.checkJson(JacksonUtils.writeValueAsString(req.getData()), JacksonUtils.writeValueAsString(req.getRules()));
    }

    private List<List<String>> head(List<DataSetSchema> fields) {
        List<List<String>> list = new ArrayList<>();
        for (DataSetSchema field : fields) {
            list.add(Collections.singletonList(field.getField()));
        }
        return list;
    }
}
