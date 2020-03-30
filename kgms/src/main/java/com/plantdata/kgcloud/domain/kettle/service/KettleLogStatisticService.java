package com.plantdata.kgcloud.domain.kettle.service;

import com.plantdata.kgcloud.domain.dw.req.KettleLogStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.rsp.KettleLogStatisticRsp;

import java.util.List;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 10:06
 **/
public interface KettleLogStatisticService {
    /**
     * Kettle日志统计
     * @param dataId 数仓id
     * @param statisticReq @link KettleLogStatisticReq
     * @return ..
     */
    KettleLogStatisticRsp kettleLogStatisticByDate(long dataId,KettleLogStatisticReq statisticReq);

    /**
     * 填充统计数据
     * @param mapRspList
     */
    void fillGraphMapRspCount(List<GraphMapRsp> mapRspList);

}
