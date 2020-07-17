package ai.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.kg.api.edit.resp.BatchEntityVO;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.edit.util.MetaDataUtils;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;

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

        if(copy.getAttrValueMetaData() == null){
            copy.setAttrValueMetaData(new HashMap<>());
        }

        if(entityRsp.getMetaDataMap() != null && !entityRsp.getMetaDataMap().isEmpty()){
            copy.getMetaData().putAll(entityRsp.getMetaDataMap());
        }
        MetaDataUtils.getDefaultSourceMetaData(copy.getMetaData(), SessionHolder.getUserId());
        MetaDataUtils.getDefaultAttrSourceMetaData(entityRsp,copy.getAttrValueMetaData(), SessionHolder.getUserId());
        return copy;
    }
}
