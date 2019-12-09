package com.plantdata.kgcloud.domain.dataset.service;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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


    private DataOptProvider getProvider(Long datasetId) {
        DataSet one = dataSetService.findOne(datasetId);
        DataOptConnect connect = DataOptConnect.of(one);
        return DataOptProviderFactory.createProvider(connect, one.getDataType());
    }


    @Override
    public Page<Map<String, Object>> getData(Long datasetId, DataOptQueryReq req) {

//        if (Objects.nonNull(req.getCreateAtBegin())) {
//            Date startTimeOfDate = DateUtils.getStartTimeOfDate(req.getCreateAtBegin());
//        }
        try (DataOptProvider provider = getProvider(datasetId)) {
            PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
            List<Map<String, Object>> maps = provider.find(req.getOffset(), req.getLimit(), null);
            long count = provider.count(null);
            return new PageImpl<>(maps, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> getDataById(Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            return provider.findOne(dataId);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> insertData(Long datasetId, Map<String, Object> data) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if ("_id".equals(entry.getKey())) {
                    objectNode.putPOJO(entry.getKey(), entry.getValue());
                }
            }
            return provider.insert(objectNode);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> updateData(Long datasetId, String dataId, Map<String, Object> data) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                objectNode.putPOJO(entry.getKey(), entry.getValue());
            }
            return provider.update(dataId, objectNode);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void deleteData(Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            provider.delete(dataId);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void deleteAll(Long datasetId) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            provider.deleteAll();
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void batchDeleteData(Long datasetId, Collection<String> dataIds) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            provider.batchDelete(dataIds);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public void exportData(Long datasetId, HttpServletResponse response) {
        DataSet one = dataSetService.findOne(datasetId);


        try (DataOptProvider provider = getProvider(datasetId)) {
            List<Map<String, Object>> mapList = provider.find(null, null, null);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String dataName = one.getDataName() + "_" + System.currentTimeMillis();
            String fileName = URLEncoder.encode(dataName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).head(head(one.getFields())).sheet().doWrite(mapList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<List<String>> head(List<String> fields) {
        List<List<String>> list = new ArrayList<>();
        for (String field : fields) {
            list.add(Collections.singletonList(field));
        }
        return list;
    }
}
