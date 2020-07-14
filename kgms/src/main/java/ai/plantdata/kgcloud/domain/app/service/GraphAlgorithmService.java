package ai.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 9:51
 */
public interface GraphAlgorithmService {

    BusinessGraphRsp run(String kgName, Long id, BusinessGraphRsp graphBean);

    AlgorithmStatisticeRsp runStatistics(String kgName, long id, BusinessGraphRsp graphBean);
}
