package ai.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;

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

        if(!metaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())){
            metaData.put(MetaDataInfo.SOURCE_USER.getFieldName(), userId);
        }

        return metaData;
    }

    public static void getDefaultAttrSourceMetaData(OpenBatchSaveEntityRsp entityRsp, Map<Integer, Map<String, Object>> attrValueMetaData, String userId) {

        Map<Integer,String> attr = entityRsp.getAttributes();

        if(attr == null || attr.isEmpty()){
            return;
        }

        for(Integer key : attr.keySet()){

            if(!attrValueMetaData.containsKey(key)){

                Map<String, Object> attrMetaData = new HashMap<>();
                attrMetaData.put(MetaDataInfo.SOURCE_USER.getFieldName(), userId);
                attrValueMetaData.put(key,attrMetaData);

            }else{

                Map<String, Object> attrMetaData = attrValueMetaData.get(key);
                if(!attrMetaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())){
                    attrMetaData.put(MetaDataInfo.SOURCE_USER.getFieldName(), userId);
                }

            }

        }
    }
}
