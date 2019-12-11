package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 18:43
 **/
@Service
public class DataOptServiceImpl implements DataOptService {

    @Autowired
    private DataSetService dataSetService;

    private DataOptProvider getProvider(String userId, Long datasetId) {
        DataSet one = dataSetService.findOne(userId, datasetId);
        DataOptConnect connect = DataOptConnect.of(one);
        return DataOptProviderFactory.createProvider(connect, one.getDataType());
    }

    @Override
    public Page<Map<String, Object>> getData(String userId, Long datasetId, DataOptQueryReq req) {

//        if (Objects.nonNull(req.getCreateAtBegin())) {
//            Date startTimeOfDate = DateUtils.getStartTimeOfDate(req.getCreateAtBegin());
//        }
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
            List<Map<String, Object>> maps = provider.find(req.getOffset(), req.getLimit(), null);
            long count = provider.count(null);
            return new PageImpl<>(maps, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> getDataById(String userId, Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            return provider.findOne(dataId);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> insertData(String userId, Long datasetId, Map<String, Object> data) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            return provider.insert(createNode(data));
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void upload(String userId, Long datasetId, MultipartFile file) throws Exception {
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
                mapList.add(map);
                if (mapList.size() == 10000) {
                    batchInsertData(userId, datasetId, mapList);
                    mapList.clear();
                }
                if (!mapList.isEmpty()) {
                    batchInsertData(userId, datasetId, mapList);
                    mapList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet().doRead();
    }

    @Override
    public Map<String, Object> updateData(String userId, Long datasetId, String dataId, Map<String, Object> data) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                objectNode.putPOJO(entry.getKey(), entry.getValue());
            }
            return provider.update(dataId, objectNode);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    private JsonNode createNode(Map<String, Object> map) {
        ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!"_id".equals(entry.getKey())) {
                objectNode.putPOJO(entry.getKey(), entry.getValue());
            }
        }
        return objectNode;
    }

    @Override
    public void batchInsertData(String userId, Long datasetId, List<Map<String, Object>> dataList) {
        try (DataOptProvider provider = getProvider(userId, datasetId)) {
            List<JsonNode> jsonNodes = new ArrayList<>();
            for (Map<String, Object> objectMap : dataList) {
                jsonNodes.add(createNode(objectMap));
            }
            provider.batchInsert(jsonNodes);
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
            List<Map<String, Object>> mapList = provider.find(null, null, null);
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


    private List<List<String>> head(List<DataSetSchema> fields) {
        List<List<String>> list = new ArrayList<>();
        for (DataSetSchema field : fields) {
            list.add(Collections.singletonList(field.getField()));
        }
        return list;
    }
}
