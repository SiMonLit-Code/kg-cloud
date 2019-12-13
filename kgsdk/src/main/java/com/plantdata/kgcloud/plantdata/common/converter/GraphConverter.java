package com.plantdata.kgcloud.plantdata.common.converter;

import com.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 13:46
 */
public class GraphConverter extends ConverterUtils {

    public static InitGraphBean graphInitRspToInitGraphBean(@NonNull GraphInitRsp graphInitRsp) {
        InitGraphBean initGraphBean = new InitGraphBean();
        initGraphBean.setConfig(graphInitRsp.getConfig());
        initGraphBean.setCreateTime(graphInitRsp.getCreateTime());
        List<InitGraphBean.InitGraphEntity> initGraphEntities = listToRsp(graphInitRsp.getEntities(), GraphConverter::graphInitEntityRspToInitGraphEntity);
        initGraphBean.setEntities(initGraphEntities);
        initGraphBean.setCreateTime(graphInitRsp.getCreateTime());
        initGraphBean.setKgName(graphInitRsp.getKgName());
        initGraphBean.setType(graphInitRsp.getType().getValue());
        initGraphBean.setUpdateTime(graphInitRsp.getUpdateTime());
        return initGraphBean;
    }

    private static InitGraphBean.InitGraphEntity graphInitEntityRspToInitGraphEntity(@NonNull GraphInitRsp.GraphInitEntityRsp initEntityRsp) {
        InitGraphBean.InitGraphEntity initGraphEntity = new InitGraphBean.InitGraphEntity();
        initGraphEntity.setClassId(initEntityRsp.getClassId());
        initGraphEntity.setId(initEntityRsp.getId().intValue());
        initGraphEntity.setName(initEntityRsp.getName());
        return initGraphEntity;
    }
}
