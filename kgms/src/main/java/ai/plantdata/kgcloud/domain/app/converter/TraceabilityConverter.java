package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.BasicInfoListFrom;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.sdk.req.app.TraceabilityQueryReq;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-07 18:06
 **/
@Slf4j
public class TraceabilityConverter {

    private static String DATANAME = "dataName";

    private static String TABLENAME = "tableName";

    private static String DATA_ID = "id";

    private static String PREFIX = MetaDataInfo.METADATA + MetaDataInfo.TRUE_SOURCE.getCode()+".";

    public static BasicInfoListFrom traceabilityToMetaDataQuery(TraceabilityQueryReq req){

        BasicInfoListFrom basicInfoListFrom = new BasicInfoListFrom();
        basicInfoListFrom.setConceptId(0L);
        basicInfoListFrom.setInherit(true);
        basicInfoListFrom.setType(1);
        basicInfoListFrom.setMetaData(traceabilityToMeta(req));

        log.error("BasicInfoListFrom:{}", JsonUtils.objToJson(basicInfoListFrom));
        return basicInfoListFrom;
    }

    private static Map<String, Object> traceabilityToMeta(TraceabilityQueryReq req) {

        Map<String, Object> filters = new HashMap<>();

        filters.put(PREFIX+DATANAME, req.getDataName());
        filters.put(PREFIX+TABLENAME, req.getTableName());
        filters.put(PREFIX+DATA_ID, req.getId());
        return filters;

    }


}
