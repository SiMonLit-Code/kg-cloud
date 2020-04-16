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
import com.plantdata.kgcloud.util.DateUtils;
import lombok.NonNull;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.LinkedHashMap;
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
    private static String SUM = "sum";

    public static List<Bson> buildAggMap(KettleLogStatisticReq statisticReq, long dataId) {
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
        long startTime = DateUtils.parseDatetime(statisticReq.getStartDate()).getTime();
        long endTime = DateUtils.parseDatetime(statisticReq.getEndDate()).getTime();
        Bson match = and(basicBSONObject, gte("logTimeStamp", startTime),
                lte("logTimeStamp", endTime),
                eq("time_flag", KettleLogStatisticTypeEnum.HOUR.getLowerCase()),
                eq("dbId", dataId),
                in("tbName", statisticReq.getTableName()));

        BasicDBObject basicDBObject = new BasicDBObject("tbName", "$tbName")
                .append("date", "$logTimeStamp")
                .append("dbId", "$dbId");
        list.add(Aggregates.match(match));
        list.add(Aggregates.group(basicDBObject, Accumulators.sum(SUM, "$W")));
        return list;
    }


    public static MongoCollection<Document> getCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase(DB_NAME).getCollection(TB_NAME);
    }


    public static KettleLogStatisticRsp parseKettleLogStatisticRsp(@NonNull MongoCursor<Document> data, @NonNull KettleLogStatisticTypeEnum statisticType, @NonNull List<String> tableNames) {


        List<KettleLogAggResultDTO> list = Lists.newArrayList();
        data.forEachRemaining(a -> {
            KettleLogAggResultDTO resultDTO = new KettleLogAggResultDTO();
            Document idMap = a.get("_id", Document.class);
            resultDTO.setSum(a.getLong(SUM));
            resultDTO.set_id(new KettleLogAggResultDTO.IdClass(new Date(idMap.getLong("date")), idMap.getString("tbName"), idMap.getLong("dbId")));
            list.add(resultDTO);
        });

        Map<String, List<KettleLogAggResultDTO>> groupDataMap = list.stream()
                .collect(Collectors.groupingBy(a -> KettleLogAggResultDTO.formatByStatisticType(a.get_id().getDate(), statisticType)));

        LinkedHashMap<String, List<KettleLogStatisticRsp.MeasureRsp>> map = new LinkedHashMap<>();
        groupDataMap.forEach((key, value) -> {
            Map<String, List<KettleLogAggResultDTO>> nameGroupMap = value.stream()
                    .collect(Collectors.groupingBy(a -> a.get_id().getTbName()));
            List<KettleLogStatisticRsp.MeasureRsp> collect = tableNames.stream().map(a -> {
                List<KettleLogAggResultDTO> resultDTOList = nameGroupMap.get(a);
                if (resultDTOList == null) {
                    return new KettleLogStatisticRsp.MeasureRsp(a, 0L);
                }
                long sum = resultDTOList.stream().mapToLong(KettleLogAggResultDTO::getSum).sum();
                return new KettleLogStatisticRsp.MeasureRsp(a, sum);
            }).collect(Collectors.toList());
            map.put(key, collect);
        });

        return new KettleLogStatisticRsp(list.get(0).get_id().getDate(), map);
    }
}
