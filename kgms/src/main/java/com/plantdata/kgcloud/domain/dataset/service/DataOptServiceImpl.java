package com.plantdata.kgcloud.domain.dataset.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
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


    private DataOptProvider getProvider(Long datasetId) {
        DataSet one = dataSetService.findOne(datasetId);
        DataOptConnect connect = DataOptConnect.of(one);
        return DataOptProviderFactory.createProvider(connect, one.getDataType());
    }


    @Override
    public List<Map<String, Object>> getData(Long datasetId, DataOptQueryReq req) {

        if (Objects.nonNull(req.getCreateAtBegin())) {
            Date startTimeOfDate = DateUtils.getStartTimeOfDate(req.getCreateAtBegin());
        }
        try (DataOptProvider provider = getProvider(datasetId)) {

            return null;
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> getDataById(Long datasetId, String dataId) {
        try (DataOptProvider provider = getProvider(datasetId)) {

            return null;
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> insertData(Long datasetId, JsonNode data) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            return provider.insert(data);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public Map<String, Object> updateData(Long datasetId, String dataId, JsonNode data) {
        try (DataOptProvider provider = getProvider(datasetId)) {
            return provider.update(dataId, data);
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
}
