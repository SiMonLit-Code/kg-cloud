package com.plantdata.kgcloud.domain.kettle.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.plantdata.kgcloud.constant.KettleLogStatisticTypeEnum;
import com.plantdata.kgcloud.domain.dw.req.KettleLogStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.rsp.KettleLogStatisticRsp;
import com.plantdata.kgcloud.domain.kettle.KettleLogDeal;
import com.plantdata.kgcloud.domain.kettle.service.KettleLogStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 10:09
 **/
@Service
@Slf4j
public class KettleLogStatisticServiceImpl implements KettleLogStatisticService {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public KettleLogStatisticRsp kettleLogStatisticByDate(long dataId, KettleLogStatisticReq statisticReq) {
        List<Bson> aggList = KettleLogDeal.buildAggMap(statisticReq, dataId);
        aggList.forEach(a -> log.info(a.toString()));
        MongoCursor<Document> iterator = KettleLogDeal.getCollection(mongoClient).aggregate(aggList).iterator();
        if (iterator.hasNext()) {
            return KettleLogDeal.parseKettleLogStatisticRsp(iterator, statisticReq.getTableName());
        }
        return KettleLogStatisticRsp.EMPTY;
    }

    @Override
    public void fillGraphMapRspCount(List<GraphMapRsp> mapRspList) {
        Map<Long, List<GraphMapRsp>> collect = mapRspList.stream()
                .collect(Collectors.groupingBy(GraphMapRsp::getDataBaseId));

        collect.forEach((k, v) -> {
            Set<String> tableNames = v.stream().map(GraphMapRsp::getTableName).collect(Collectors.toSet());
            Bson query = and(eq("dbId", v.get(0).getDataBaseId()),
                    eq("time_flag", KettleLogStatisticTypeEnum.DAY.getLowerCase()),
                    in("tbName", tableNames));
            FindIterable<Document> projection = KettleLogDeal.getCollection(mongoClient)
                    .find(query)
                    .sort(Sorts.descending("logTimeStamp"))
                    .projection(new Document("tbName", 1L).append("logTimeStamp", 1L).append("W", 1L));
            fillCount(projection, v);
        });
    }

    private void fillCount(FindIterable<Document> projection, List<GraphMapRsp> rspList) {
        MongoCursor<Document> iterator = projection.iterator();
        if (!iterator.hasNext() || CollectionUtils.isEmpty(rspList)) {
            return;
        }
        Map<String, String> countByTable = new HashMap<>();
        Map<String, Long> statisticMap = new HashMap<>();
        Map<String, List<GraphMapRsp>> tableMap = rspList.stream().collect(Collectors.groupingBy(GraphMapRsp::getTableName));
        while (iterator.hasNext()) {
            Document next = iterator.next();
            String tableName = next.getString("tbName");
            String logTimeStamp = countByTable.get(tableName);

            String formatDate = next.getString("logTimeStamp");
            if (logTimeStamp == null) {
                countByTable.put(tableName, formatDate);
                statisticMap.put(tableName, next.getLong("W"));
            }
            if (logTimeStamp != null && logTimeStamp.equals(formatDate)) {
                Long orDefault = statisticMap.get(tableName);
                statisticMap.put(tableName, orDefault + next.getLong("W"));
            }
        }
        tableMap.forEach((k1, v1) -> {
            Long sum = statisticMap.getOrDefault(k1, 0L);
            String date = countByTable.get(k1);
            v1.forEach(val -> {
                val.setLastDate(date);
                val.setLastDateCount(sum);
            });
        });
    }

}
