package com.plantdata.kgcloud.plantdata.converter.app;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.AssociationParameter;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.rsp.app.ApkBean;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 19:52
 */
public class AppConverter extends BasicConverter {

    public static KnowledgeRecommendReq associationParameterToKnowledgeRecommendReq(@NonNull AssociationParameter param) {
        KnowledgeRecommendReq recommendReq = new KnowledgeRecommendReq();
        recommendReq.setAllowAttrs(param.getAllowAtts());
        recommendReq.setAllowAttrsKey(param.getAllowAttsKey());
        recommendReq.setDirection(param.getDirection());
        recommendReq.setEntityId(param.getEntityId());
        recommendReq.setPage(NumberUtils.INTEGER_ONE);
        recommendReq.setSize(param.getPageSize());
        return recommendReq;
    }

    public static KVBean<String, List<EntityBean>> infoBoxAttrRspToKvBean(@NonNull ObjectAttributeRsp attrRsp) {
        List<EntityBean> entityBeans = listToRsp(attrRsp.getEntityList(), PromptConverter::promptEntityRspToEntityBean);
        return new KVBean<>(String.valueOf(attrRsp.getAttrDefId()), entityBeans);
    }

    public static ApkBean apkRspToApkBean(@NonNull ApkRsp apkRsp) {
        ApkBean apkBean = new ApkBean();
        apkBean.setApk(apkRsp.getApk());
        apkBean.setKgName(apkRsp.getKgName());
        apkBean.setTitle(apkRsp.getTitle());
        return apkBean;
    }
}
