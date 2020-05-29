package com.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.kg.api.edit.resp.BatchEntityVO;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.edit.util.MetaDataUtils;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.security.SessionHolder;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class OpenEntityConverter extends BasicConverter {

    public static BatchEntityVO openBatchSaveEntityRspToVo(@NotNull OpenBatchSaveEntityRsp entityRsp) {
        BatchEntityVO copy = copy(entityRsp, BatchEntityVO.class);
        consumerIfNoNull(entityRsp.getMetaData(), a -> {
            Map<String, Object> metaMap = entityRsp.getMetaDataMap();
            if(metaMap == null){
                metaMap = new HashMap<>();
            }

            if(a.getBatchNo() != null){
                metaMap.put(MetaDataInfo.BATCH_NO.getFieldName(), a.getBatchNo());
            }

            copy.setMetaData(metaMap);
        });
        if(copy.getMetaData() == null){
            copy.setMetaData(new HashMap<>());
        }
        MetaDataUtils.getDefaultSourceMetaData(copy.getMetaData(), SessionHolder.getUserId());
        return copy;
    }
}
