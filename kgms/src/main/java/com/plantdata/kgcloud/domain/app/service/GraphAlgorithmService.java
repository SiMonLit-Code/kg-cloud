package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 9:51
 */
public interface GraphAlgorithmService {

    BusinessGraphRsp run(String kgName, Long id, BusinessGraphRsp graphBean);
}
