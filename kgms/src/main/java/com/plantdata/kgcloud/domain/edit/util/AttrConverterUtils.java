package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.api.edit.resp.AttributeDefinitionVO;
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
    public static AttributeDefinitionVO attrDefinitionReqConvert(AttrDefinitionReq attrDefinitionReq) {
        AttributeDefinitionVO vo =
                ConvertUtils.convert(AttributeDefinitionVO.class).apply(attrDefinitionReq);
        vo.setAdditionalInfo(JacksonUtils.writeValueAsString(attrDefinitionReq.getAdditionalInfo()));
        vo.setConstraints(JacksonUtils.writeValueAsString(attrDefinitionReq.getConstraints()));
        return vo;
    }

}
