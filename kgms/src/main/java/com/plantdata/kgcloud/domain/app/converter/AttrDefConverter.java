package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.common.bean.AttributeDefinition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.req.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.sdk.req.app.AttrDefQueryReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttrExtraRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:53
 */
@Slf4j
public class AttrDefConverter extends BasicConverter {
    /**
     * 属性定义 vo转return
     */
    public static AttributeDefinitionRsp attrDefToAttrDefRsp(@NonNull AttributeDefinition att) {
        AttributeDefinitionRsp attrDefReq = new AttributeDefinitionRsp();
        attrDefReq.setId(att.getId());
        attrDefReq.setName(att.getName());
        attrDefReq.setKey(att.getKey());
        attrDefReq.setType(att.getType());
        attrDefReq.setDirection(att.getDirection());
        attrDefReq.setRangeValue(att.getRangeValue());
        attrDefReq.setDomainValue(att.getDomainValue());
        attrDefReq.setDataType(att.getDataType());
        consumerIfNoNull(att.getExtraInfo(), a -> {
            List<AttrExtraRsp> extraInfoItemList = listConvert(a, b -> {
                AttrExtraRsp infoItem = new AttrExtraRsp();
                //!!要求属性名称一致
                BeanUtils.copyProperties(b, infoItem);
                return infoItem;
            });
            attrDefReq.setExtraInfos(extraInfoItemList);
        });
        try {
            consumerIfNoNull(att.getAdditionalInfo(), a -> attrDefReq.setAdditionalInfo(JacksonUtils.readValue(a, new TypeReference<Map<String, Object>>() {
            })));
        } catch (Exception e) {
            log.error("additionalInfo:{}", att.getAdditionalInfo());
            e.printStackTrace();
        }

        return attrDefReq;
    }

    public static AttrDefinitionSearchReq attrDefQueryReqToAttrDefinitionSearchReq(AttrDefQueryReq queryReq) {
        AttrDefinitionSearchReq searchReq = new AttrDefinitionSearchReq();
        searchReq.setConceptId(queryReq.getConceptId());
        searchReq.setInherit(queryReq.isInherit());
        searchReq.setType(NumberUtils.INTEGER_ZERO);
        return searchReq;
    }


}
