package ai.plantdata.kgcloud.plantdata.converter.graph;

import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.plantdata.rsp.app.InitGraphBean;
import ai.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 13:46
 */
public class GraphInitBasicConverter extends BasicConverter {

    public static InitGraphBean graphInitRspToInitGraphBean(@NonNull GraphInitRsp graphInitRsp) {
        InitGraphBean initGraphBean = new InitGraphBean();
        initGraphBean.setConfig(graphInitRsp.getConfig());
        initGraphBean.setCreateTime(graphInitRsp.getCreateTime());
        List<InitGraphBean.InitGraphEntity> initGraphEntities = toListNoNull(graphInitRsp.getEntities(), GraphInitBasicConverter::graphInitEntityRspToInitGraphEntity);
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
        initGraphEntity.setId(initEntityRsp.getId());
        initGraphEntity.setName(initEntityRsp.getName());
        return initGraphEntity;
    }
}
