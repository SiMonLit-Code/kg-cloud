package com.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.kg.api.edit.resp.BatchEntityVO;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class OpenEntityConverter extends BasicConverter {

    public static BatchEntityVO openBatchSaveEntityRspToVo(@NotNull OpenBatchSaveEntityRsp entityRsp) {
        BatchEntityVO copy = copy(entityRsp, BatchEntityVO.class);
        consumerIfNoNull(entityRsp.getMetaData(), a -> {
            Map<String, Object> metaMap = Maps.newHashMap();
            consumerIfNoNull(a.getBatchNo(), b -> metaMap.put(MetaDataInfo.BATCH_NO.getFieldName(), b));
            copy.setMetaData(metaMap);
        });
        return copy;
    }
}
