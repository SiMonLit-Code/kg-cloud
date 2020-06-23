package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.api.edit.req.EdgeFrom;
import ai.plantdata.kg.api.edit.resp.AttributeDefinitionVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionReq;
import com.plantdata.kgcloud.sdk.req.edit.ExtraInfoReq;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        vo.setExtraInfo(extraInfoReqConvert(attrDefinitionReq.getExtraInfoList()));
        return vo;
    }

    private static List<EdgeFrom> extraInfoReqConvert(List<ExtraInfoReq> extraInfoList) {

        if(extraInfoList == null || extraInfoList.isEmpty()){
            return Lists.newArrayList();
        }

        return extraInfoList.stream().map(e -> {
            EdgeFrom edgeFrom = ConvertUtils.convert(EdgeFrom.class).apply(e);

            try {
                edgeFrom.setObjRange(Lists.newArrayList(Long.valueOf(e.getObjRange())));
            }catch (Exception ex){}

            return edgeFrom;
        }).collect(Collectors.toList());

    }

    public static AttributeDefinitionVO attrDefinitionReqConvert(AttrDefinitionModifyReq modifyReq) {
        AttributeDefinitionVO vo =
                ConvertUtils.convert(AttributeDefinitionVO.class).apply(modifyReq);
        vo.setAdditionalInfo(JacksonUtils.writeValueAsString(modifyReq.getAdditionalInfo()));
        vo.setConstraints(JacksonUtils.writeValueAsString(modifyReq.getConstraints()));
        return vo;
    }
}
