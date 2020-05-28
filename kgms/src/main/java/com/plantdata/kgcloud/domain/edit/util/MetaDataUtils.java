package com.plantdata.kgcloud.domain.edit.util;

import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.security.SessionHolder;
import org.terracotta.offheapstore.MetadataTuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-28 10:20
 **/
public class MetaDataUtils {

    public static Map<String, Object> getDefaultSourceMetaData(Map<String,Object> metaData, String userId) {

        if(metaData == null){
            metaData = new HashMap<>();
        }

        if(!metaData.containsKey(MetaDataInfo.SOURCE.getFieldName())){
            metaData.put(MetaDataInfo.SOURCE.getFieldName(), "用户写入");
        }

        if(!metaData.containsKey(MetaDataInfo.TRUE_SOURCE.getFieldName())){
            Map<String,Object> trueSource = new HashMap<>();
            trueSource.put("source","用户写入");
            metaData.put(MetaDataInfo.TRUE_SOURCE.getFieldName(), trueSource);
        }

        if(!metaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())){
            metaData.put(MetaDataInfo.SOURCE_USER.getFieldName(), userId);
        }

        if(!metaData.containsKey(MetaDataInfo.SOURCE_ACTION.getFieldName())){
            metaData.put(MetaDataInfo.SOURCE_ACTION.getFieldName(), "在线编辑");
        }
        return metaData;
    }
}
