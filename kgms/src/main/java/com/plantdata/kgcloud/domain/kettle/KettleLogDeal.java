package com.plantdata.kgcloud.domain.kettle;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.plantdata.kgcloud.constant.KettleLogStatisticTypeEnum;
import com.plantdata.kgcloud.constant.KettleLogTypeEnum;
import com.plantdata.kgcloud.domain.dw.req.KettleLogStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.KettleLogStatisticRsp;
import com.plantdata.kgcloud.domain.kettle.dto.KettleLogAggResultDTO;
import lombok.NonNull;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 10:12
 **/
public class KettleLogDeal {

    private static String DB_NAME = "kettle_logs";
    private static String TB_NAME = "logs_data";
    private static String SUM="sum";
    public static List<Bson> buildAggMap(KettleLogStatisticReq statisticReq) {
        List<Bson> list = Lists.newArrayListWithExpectedSize(2);
        BasicDBObject basicBSONObject = new BasicDBObject();
        if (statisticReq.getDataType().compareTo(KettleLogTypeEnum.ALL) != 0) {
            if (statisticReq.getDataType().compareTo(KettleLogTypeEnum.SUCCESS) == 0) {
                basicBSONObject = new BasicDBObject("is_error", false);
            }
            if (statisticReq.getDataType().compareTo(KettleLogTypeEnum.ERROR) != 0) {
                basicBSONObject = new BasicDBObject("is_error", true);
            }
        }
        Bson match = and(basicBSONObject, gte("logTimeStamp", statisticReq.getStartDate()),
                lte("logTimeStamp", statisticReq.getEndDate()),
                eq("time_flag", statisticReq.getStatisticType().getLowerCase()));

        BasicDBObject basicDBObject = new BasicDBObject("resourceName", "$resourceName")
                .append("date", "$logTimeStamp");
        list.add(Aggregates.match(match));
        list.add(Aggregates.group(basicDBObject, Accumulators.sum(SUM, "$W")));
        return list;
    }


    public static MongoCollection<Document> getCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase(DB_NAME).getCollection(TB_NAME);
    }


    public static KettleLogStatisticRsp parseKettleLogStatisticRsp(@NonNull MongoCursor<Document> data, long dataStoreId, @NonNull List<String> tableNames) {

        List<KettleLogAggResultDTO> list = Lists.newArrayList();
        data.forEachRemaining(a -> {
            KettleLogAggResultDTO resultDTO = new KettleLogAggResultDTO();
            Document idMap = a.get("_id", Document.class);
            resultDTO.setSum(a.getLong(SUM));
            resultDTO.set_id(new KettleLogAggResultDTO.IdClass(idMap.getString("date"), idMap.getString("resourceName")));
            list.add(resultDTO);
        });

        Map<String, List<KettleLogAggResultDTO>> groupDataMap = list.stream()
                .filter(a ->
                        tableNames.stream()
                                .map(str -> "ktr_" + dataStoreId + "_" + str + "_isAll")
                                .anyMatch(temp -> a.get_id().getResourceName().startsWith(temp))
                )
                .collect(Collectors.groupingBy(a -> a.get_id().getDate()));

        Map<String, List<KettleLogStatisticRsp.MeasureRsp>> resMap = groupDataMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<String, List<KettleLogAggResultDTO>> nameGroupMap = entry.getValue().stream().collect(Collectors.groupingBy(a -> getTableName(a.get_id().getResourceName())));
                            return nameGroupMap.entrySet().stream()
                                    .map(a -> {
                                        long sum = a.getValue().stream().mapToLong(KettleLogAggResultDTO::getSum).sum();
                                        return new KettleLogStatisticRsp.MeasureRsp(a.getKey(), sum);
                                    })
                                    .collect(Collectors.toList());
                        })
                );
        return new KettleLogStatisticRsp(resMap);
    }

    public static String getTableName(String str) {
        String[] s = str.split("_");
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i < s.length; i++) {
            if (s[i].equals("isAll")) {
                break;
            }
            stringBuilder.append(s[i]);
        }
        return stringBuilder.toString();
    }


}
