package com.plantdata.kgcloud.util;

import com.plantdata.kgcloud.plantdata.constant.MetaDataEnum;
import com.plantdata.kgcloud.security.SessionHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-28 10:12
 **/
public class MetaDataUtils {



    public static Map<String,Object> getDefaultMetaData(Map<String,Object> metaData) {

        if(metaData == null){
            metaData = new HashMap<>();
        }

        if(!metaData.containsKey(MetaDataEnum.source.getValue())){
            metaData.put(MetaDataEnum.source.getValue(),"接口写入");
        }

        if(!metaData.containsKey(MetaDataEnum.trueSource.getValue())){
            Map<String,Object> trueSource = new HashMap<>();
            trueSource.put("source","接口写入");
            metaData.put(MetaDataEnum.trueSource.getValue(), trueSource);
        }

//        metaData.put(MetaDataEnum.sourceUser.getValue(), userId);
        metaData.put(MetaDataEnum.sourceAction.getValue(),"API接口");

        return metaData;
    }

}
