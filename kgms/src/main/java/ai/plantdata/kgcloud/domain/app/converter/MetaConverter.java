package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.cloud.util.DateUtils;
import ai.plantdata.kgcloud.constant.MetaDataInfo;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import ai.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.OriginRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 11:27
 */
public class MetaConverter extends BasicConverter {


    public static void fillMetaWithNoNull(Map<String, Object> metaData, MetaDataInterface metaDataImpl) {
        if (metaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.SCORE.getFieldName()),
                    a -> metaDataImpl.setScore(Double.parseDouble(a.toString())));
        }
        if (metaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.RELIABILITY.getFieldName()),
                    a -> metaDataImpl.setReliability(Double.parseDouble(a.toString())));
        }
        if (metaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.BATCH_NO.getFieldName()),
                    a -> metaDataImpl.setBatch(a.toString()));
        }
        if (metaData.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.FROM_TIME.getFieldName()),
                    a -> metaDataImpl.setStartTime(parseDate(a.toString())));
        }
        if (metaData.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.TO_TIME.getFieldName()),
                    a -> metaDataImpl.setEndTime(parseDate(a.toString())));
        }
        if (metaData.containsKey(MetaDataInfo.ENTITY_LINK.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.ENTITY_LINK.getFieldName()),
                    a -> metaDataImpl.setEntityLinks(JsonUtils.objToList(a, EntityLinkVO.class)));

        }
        if (metaData.containsKey(MetaDataInfo.OPEN_GIS.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.OPEN_GIS.getFieldName()),
                    a -> metaDataImpl.setOpenGis((Boolean) a));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_COORDINATE.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName()),
                    a -> BasicConverter.consumerIfNoNull(JsonUtils.objToList(a, Double.class), list -> {
                        metaDataImpl.setLat(list.get(1));
                        metaDataImpl.setLng(list.get(0));
                    }));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_ADDRESS.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.GIS_ADDRESS.getFieldName()),
                    a -> metaDataImpl.setAddress((String) a));
        }
        if (metaData.containsKey(MetaDataInfo.SOURCE.getFieldName()) || metaData.containsKey(MetaDataInfo.SOURCE_REASON.getFieldName())) {
            OriginRsp originRsp = new OriginRsp();
            consumerIfNoNull(metaData.get(MetaDataInfo.SOURCE.getFieldName()), a -> originRsp.setSource(a.toString()));
            consumerIfNoNull(metaData.get(MetaDataInfo.SOURCE_REASON.getFieldName()), a -> originRsp.setSourceReason(a.toString()));
            metaDataImpl.setOrigin(originRsp);
        }

        if (metaData.containsKey(MetaDataInfo.ADDITIONAL.getFieldName())) {
            consumerIfNoNull(metaData.get(MetaDataInfo.ADDITIONAL.getFieldName()), a -> {
                AdditionalRsp additionalRsp = JsonUtils.objToNewObj(a, AdditionalRsp.class);
                consumerIfNoNull(additionalRsp, b -> {
                    metaDataImpl.setAdditional(additionalRsp);
                    consumerIfNoNull(additionalRsp.getLabelStyle(), metaDataImpl::setLabelStyle);
                    consumerIfNoNull(additionalRsp.getNodeStyle(), metaDataImpl::setNodeStyle);
                    consumerIfNoNull(additionalRsp.getLinkStyle(), metaDataImpl::setLinkStyle);
                });
            });
        }
        metaDataImpl.setTags(getTags(metaData));
    }


    public static List<TagRsp> getTags(Map<String, Object> metaDataMap) {
        if (metaDataMap.containsKey(MetaDataInfo.TAG.getFieldName())) {
            return JsonUtils.objToList(metaDataMap.get(MetaDataInfo.TAG.getFieldName()), TagRsp.class);
        }
        return Collections.emptyList();
    }

    public static Date parseDate(String str) {
        try {
            Date date = DateUtils.parseDate(str, "yyyy-MM-dd hh:mm:ss");
            if(date == null){
                date = DateUtils.parseDate(str, "yyyy-MM-dd");
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }
}
