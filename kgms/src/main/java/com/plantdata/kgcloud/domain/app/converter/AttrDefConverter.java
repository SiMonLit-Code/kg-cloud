package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.AttrQueryFrom;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeDefinitionRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.AttributeExtraInfoItem;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:53
 */
public class AttrDefConverter {
    /**
     * 属性定义 vo转return
     */
    public static List<AttributeDefinitionRsp> voToRsp(@NonNull List<AttributeDefinition> attrDefList) {
        List<AttributeDefinitionRsp> attributeDefinitionRspList = Lists.newArrayListWithCapacity(attrDefList.size());
        AttributeDefinitionRsp attrDefReq;
        for (AttributeDefinition att : attrDefList) {
            attrDefReq = new AttributeDefinitionRsp();
            attrDefReq.setId(att.getId());
            attrDefReq.setName(att.getName());
            attrDefReq.setKey(att.getKey());
            attrDefReq.setType(att.getType());
            attrDefReq.setDirection(att.getDirection());
            attrDefReq.setRange(att.getRangeValue());
            attrDefReq.setDomain(att.getDomainValue());
            attrDefReq.setDataType(att.getDataType());
            if (!CollectionUtils.isEmpty(att.getExtraInfo())) {
                List<AttributeExtraInfoItem> extraInfoItemList = att.getExtraInfo().stream().map(a -> {
                    AttributeExtraInfoItem infoItem = new AttributeExtraInfoItem();
                    //!!要求属性名称一致
                    BeanUtils.copyProperties(a, infoItem);
                    return infoItem;
                }).collect(Collectors.toList());
                attrDefReq.setExtraInfos(extraInfoItemList);
            }
            attributeDefinitionRspList.add(attrDefReq);
        }
        return attributeDefinitionRspList;
    }

    public static AttrQueryFrom convertToQuery(List<Long> conceptIdList, boolean inherit, int type) {
        AttrQueryFrom attrQueryFrom = new AttrQueryFrom();
        attrQueryFrom.setIds(conceptIdList);
        attrQueryFrom.setInherit(inherit);
        attrQueryFrom.setType(type);
        return attrQueryFrom;
    }


}
