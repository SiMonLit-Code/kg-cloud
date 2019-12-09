package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.api.edit.req.AttributeDefinitionFrom;
import ai.plantdata.kg.api.edit.resp.AttributeDefinitionVO;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

/**
 * @Author: LinHo
 * @Date: 2019/12/9 12:49
 * @Description:
 */
public class AttrConverterUtils {

    /**
     * 特定转换
     *
     * @param attrDefinitionReq
     * @return
     */
    public static AttributeDefinitionFrom attrDefinitionReqConvert(AttrDefinitionReq attrDefinitionReq) {
        AttributeDefinitionFrom attributeDefinitionFrom =
                ConvertUtils.convert(AttributeDefinitionFrom.class).apply(attrDefinitionReq);
        attributeDefinitionFrom.setAdditionalInfo(JacksonUtils.writeValueAsString(attrDefinitionReq.getAdditionalInfo()));
        attributeDefinitionFrom.setConstraints(JacksonUtils.writeValueAsString(attrDefinitionReq.getConstraints()));
        return attributeDefinitionFrom;
    }

}
