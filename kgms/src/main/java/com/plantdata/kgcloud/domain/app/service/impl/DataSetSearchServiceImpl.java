package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetOneFieldReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.LinksRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class DataSetSearchServiceImpl implements DataSetSearchService {

    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private DataSetRepository dataSetRepository;


    @Override
    public RestData<Map<String, Object>> readDataSetData(DataSet dataSet, Set<String> fields, int offset, int limit, Map<String, Object> queryMap, Map<String, Object> sortMap) {
        DataOptConnect dataOptConnect = new DataOptConnect();
        dataOptConnect.setDatabase(dataSet.getDbName());
        dataOptConnect.setTable(dataSet.getTbName());
        dataOptConnect.setAddresses(dataSet.getAddr());
        List<Map<String, Object>> maps;
        long count;
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(dataOptConnect, dataSet.getDataType())) {

            if (queryMap == null) {
                queryMap = Maps.newHashMap();
            }
            if (sortMap == null) {
                sortMap = Maps.newHashMap();
            }
            maps = provider.findWithSort(offset, limit, queryMap, sortMap);
            count = provider.count(queryMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(AppErrorCodeEnum.ERROR_DATA_SET_QUERY);
        }
        //可优化数据库层面做
        if (!CollectionUtils.isEmpty(fields) && !CollectionUtils.isEmpty(maps)) {
            maps = BasicConverter.listToRsp(maps, a -> a.entrySet().stream()
                    .filter(b -> fields.contains(b.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        return new RestData<>(maps, count);
    }

    @Override
    public Map<String, Object> readEsDataSet(List<String> addressList, List<String> databases, List<String> tables, List<String> fields,
                                             String aggs, String query, String sort, int start, int offset) {
        Map<String, Object> requestData = Maps.newHashMap();
        if (Objects.nonNull(fields) && fields.size() > 0) {
            requestData.put("_source", fields);
        }
        if (StringUtils.isNoneBlank(query)) {
            requestData.put("query", JacksonUtils.readValue(query, Object.class));
            requestData.put("from", start);
            requestData.put("size", offset);
        }
        if (StringUtils.isNoneBlank(sort)) {
            requestData.put("sort", JacksonUtils.readValue(sort, Object.class));
        }

        if (StringUtils.isNoneBlank(aggs)) {
            requestData.put("aggs", JSON.parse(aggs));
        }
        log.warn("es:" + JacksonUtils.writeValueAsString(requestData));
        String rs = EsUtils.sendPost(EsUtils.buildEsQuery(addressList), databases, tables, JacksonUtils.writeValueAsString(requestData));
        return JacksonUtils.readValue(rs, new TypeReference<Map<String, Object>>() {
        });
    }


    @Override
    public List<DataLinkRsp> getDataLinks(String kgName, String userId, Long entityId) {
        MongoQueryFrom mongoQueryFrom = buildMongoQuery(kgName, entityId);

        Optional<List<Map<String, Object>>> opt = RestRespConverter.convert(mongoApi.postJson(mongoQueryFrom));
        if (!opt.isPresent() || CollectionUtils.isEmpty(opt.get())) {
            return Collections.emptyList();
        }
        List<DataLinkRsp> dataLinks = new ArrayList<>();


        for (Map<String, Object> map : opt.get()) {
            //最多显示5条
            if (dataLinks.size() >= 5) {
                break;
            }
            Long dataSetId = Long.valueOf(map.get("_id").toString());
            mongoQueryFrom.setQuery(buildTwoQuery(entityId, dataSetId));
            Optional<List<Map<String, Object>>> dataOpt = RestRespConverter.convert(mongoApi.postJson(mongoQueryFrom));
            if (!dataOpt.isPresent()) {
                continue;
            }
            DataLinkRsp dataLinkRsp = buildDataLink(dataSetId, userId, dataOpt.get());
            BasicConverter.consumerIfNoNull(dataLinkRsp, dataLinks::add);
        }
        return dataLinks;
    }

    private DataLinkRsp buildDataLink(Long dataSetId, String userId, List<Map<String, Object>> maps) {
        DataLinkRsp dataLink = new DataLinkRsp();
        dataLink.setDataSetId(dataSetId);
        List<LinksRsp> linkList = new ArrayList<>();
        for (Map<String, Object> map1 : maps) {
            if (map1.get("data_id") == null) {
                continue;
            }
            String dataId = map1.get("data_id").toString();
            Map<String, String> myData = getDataTitle(userId, dataId, dataSetId);
            if (CollectionUtils.isEmpty(myData)) {
                continue;
            }
            if (StringUtils.isEmpty(myData.get("dataTitle"))) {
                continue;
            }
            LinksRsp links = new LinksRsp();
            links.setDataTitle(myData.get("dataTitle"));
            links.setDataId(dataId);
            BasicConverter.consumerIfNoNull(map1.get("score"), a -> links.setScore(Double.valueOf(a.toString())));
            BasicConverter.consumerIfNoNull(map1.get("source"), a -> links.setSource(Integer.valueOf(a.toString())));
            if (dataLink.getDataSetTitle() == null) {
                dataLink.setDataSetTitle(myData.get("dataSetTitle"));
            }
            linkList.add(links);
        }
        dataLink.setLinks(linkList);
        return dataLink;
    }

    private List<Map<String, Object>> buildTwoQuery(Long entityId, Long dataSetId) {
        List<Map<String, Object>> mathMapList = Lists.newArrayList();
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("data_set_id", dataSetId);
        queryMap.put("entity_id", entityId);
        mathMapList.add(DefaultUtils.oneElMap("$match", queryMap));
        mathMapList.add(DefaultUtils.oneElMap("$sort", DefaultUtils.oneElMap("score", -1)));
        mathMapList.add(DefaultUtils.oneElMap("$limit", 5));
        return mathMapList;
    }

    private static MongoQueryFrom buildMongoQuery(String kgName, long entityId) {
        MongoQueryFrom mongoQueryFrom = new MongoQueryFrom();
        mongoQueryFrom.setKgName(KGUtil.dbName(kgName));
        mongoQueryFrom.setCollection("entity_annotation");
        Map<String, Object> map = Maps.newHashMap();
        map.put("_id", "$data_set_id");
        map.put("count", DefaultUtils.oneElMap("$sum", 1));
        List<Map<String, Object>> mathMapList = Lists.newArrayList();
        mathMapList.add(DefaultUtils.oneElMap("$match", DefaultUtils.oneElMap("entity_id", entityId)));
        mathMapList.add(DefaultUtils.oneElMap("$group", map));
        mongoQueryFrom.setQuery(mathMapList);
        return mongoQueryFrom;
    }

    /**
     * 多重循环查询 改不动
     *
     * @param userId
     * @param dataId
     * @param dataSetId
     * @return
     */
    private Map<String, String> getDataTitle(String userId, String dataId, Long dataSetId) {

        if (!dataSetRepository.existsById(dataSetId)) {
            return Collections.emptyMap();
        }
        DataSet dataSet = null;
        try {
            dataSet = dataSetService.findOne(userId, dataSetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataSet == null) {
            return Collections.emptyMap();
        }
        String dataTitle = StringUtils.EMPTY;
        Optional<DataSetSchema> schemaOpt = dataSet.getSchema().stream().findAny();
        if (schemaOpt.isPresent()) {
            DataOptConnect connect = DataOptConnect.of(dataSet);
            try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, dataSet.getDataType())) {
                String key = (String) schemaOpt.get().getSettings().get("key");
                Map<String, Object> oneMap = provider.findOne(dataId);
                if (oneMap.containsKey(key)) {
                    dataTitle = String.valueOf(oneMap.get(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, String> map = Maps.newHashMapWithExpectedSize(NumberUtils.INTEGER_TWO);
        map.put("dataSetTitle", dataSet.getTitle());
        map.put("dataTitle", dataTitle);
        return map;
    }
}
