package com.plantdata.kgcloud.domain.graph.log.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.log.entity.DataLogRsp;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogReq;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogRsp;

/**
 * @author xiezhenxiang 2020/1/15
 */
public interface GraphLogService {

    BasePage<ServiceLogRsp> serviceLogList(String kgName, ServiceLogReq req);

    BasePage<DataLogRsp> dataLogList(String kgName, String batch, BaseReq req);

    BasePage<DataLogRsp> entityLogList(String kgName, Long id, Integer type, BaseReq req);

    BasePage<DataLogRsp> edgeAttrLogList(String kgName, Integer relationAttrId, BaseReq req);

    BasePage<DataLogRsp> relationLogList(String kgName, Long entityId, BaseReq req);
}
