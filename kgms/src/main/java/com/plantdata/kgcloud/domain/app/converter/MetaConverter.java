package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AdditionalRsp;
import com.plantdata.kgcloud.util.DateUtils;

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
            Object o = metaData.get(MetaDataInfo.SCORE.getFieldName());
            metaDataImpl.setScore((Double) o);
        }
        if (metaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.RELIABILITY.getFieldName());
            metaDataImpl.setReliability((Double) o);
        }
        if (metaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.BATCH_NO.getFieldName());
            metaDataImpl.setBatch(o.toString());
        }
        if (metaData.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.FROM_TIME.getFieldName());
            metaDataImpl.setStartTime(DateUtils.parseDatetime(o.toString()));
        }
        if (metaData.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.TO_TIME.getFieldName());
            metaDataImpl.setEndTime(DateUtils.parseDatetime(o.toString()));
        }
        if (metaData.containsKey(MetaDataInfo.ENTITY_LINK.getFieldName())) {
            metaDataImpl.setEntityLinks(JsonUtils.objToList(metaData.get(MetaDataInfo.ENTITY_LINK.getFieldName()), EntityLinkVO.class));
        }
        if (metaData.containsKey(MetaDataInfo.OPEN_GIS.getFieldName())) {
            metaDataImpl.setOpenGis((Boolean) metaData.get(MetaDataInfo.OPEN_GIS.getFieldName()));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_COORDINATE.getFieldName())) {
            List<Double> list = JsonUtils.objToList(metaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName()), Double.class);
            metaDataImpl.setLat(list.get(0));
            metaDataImpl.setLng(list.get(1));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_ADDRESS.getFieldName())) {
            metaDataImpl.setAddress((String) metaData.get(MetaDataInfo.GIS_ADDRESS.getFieldName()));
        }
        if (metaData.containsKey(MetaDataInfo.TAG.getFieldName())) {
            List<TagRsp> tagRspList = JsonUtils.objToList(metaData.get(MetaDataInfo.TAG.getFieldName()), TagRsp.class);
            metaDataImpl.setTags(tagRspList);
        }
        if (metaData.containsKey(MetaDataInfo.ADDITIONAL.getFieldName())) {
            AdditionalRsp additionalRsp = JsonUtils.objToNewObj(metaData.get(MetaDataInfo.ADDITIONAL.getFieldName()), AdditionalRsp.class);
            metaDataImpl.setAdditional(additionalRsp);
            if (null != additionalRsp) {
                setIfNoNull(additionalRsp.getLabelStyle(), metaDataImpl::setLabelStyle);
                setIfNoNull(additionalRsp.getNodeStyle(), metaDataImpl::setNodeStyle);
            }
        }
    }
}
