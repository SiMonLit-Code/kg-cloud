package com.plantdata.kgcloud.plantdata.converter.rule;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.rule.GraphBusinessAlgorithmBean;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 11:54
 */
public class GraphAlgorithmConverter extends BasicConverter {

    public static GraphBusinessAlgorithmBean graphConfAlgorithmRspToGraphBusinessAlgorithmBean(GraphConfAlgorithmRsp algorithmRsp) {
        GraphBusinessAlgorithmBean algorithmBean = new GraphBusinessAlgorithmBean();
        algorithmBean.setAbs(algorithmRsp.getRemark());
        algorithmBean.setCreateTime(algorithmRsp.getCreateAt());
        algorithmBean.setId(algorithmRsp.getId());
        algorithmBean.setKgName(algorithmRsp.getKgName());
        algorithmBean.setName(algorithmRsp.getAlgorithmName());
        algorithmBean.setUpdateTime(algorithmRsp.getUpdateAt());
        algorithmBean.setUrl(algorithmRsp.getAlgorithmUrl());
        algorithmBean.setType(algorithmRsp.getType());
        return algorithmBean;
    }

    public static GraphConfAlgorithmReq graphBusinessAlgorithmAddToGraphConfAlgorithmReq(GraphBusinessAlgorithmBean algorithmBean) {
        GraphConfAlgorithmReq algorithmReq = new GraphConfAlgorithmReq();
        algorithmReq.setAlgorithmName(algorithmBean.getName());
        algorithmReq.setAlgorithmUrl(algorithmBean.getUrl());
        algorithmReq.setRemark(algorithmBean.getUrl());
        algorithmReq.setType(algorithmBean.getType());
        return algorithmReq;
    }
}
