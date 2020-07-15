package ai.plantdata.kgcloud.domain.graph.log.service;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.graph.log.entity.DataLogRsp;
import ai.plantdata.kgcloud.domain.graph.log.entity.ServiceLogReq;
import ai.plantdata.kgcloud.domain.graph.log.entity.ServiceLogRsp;

/**
 * @author xiezhenxiang 2020/1/15
 */
public interface GraphLogService {

    BasePage<ServiceLogRsp> serviceLogList(String kgName, ServiceLogReq req);

    BasePage<DataLogRsp> dataLogList(String kgName, String batch, BaseReq req);

    BasePage<DataLogRsp> entityLogList(String kgName, Long id, Integer type, BaseReq req);

    BasePage<DataLogRsp> edgeAttrLogList(String kgName, Integer relationAttrId, BaseReq req);

    BasePage<DataLogRsp> relationLogList(String kgName, Long entityId, BaseReq req);

    BasePage<DataLogRsp> attrDefineLogList(String kgName, Integer attrId, BaseReq req);
}
