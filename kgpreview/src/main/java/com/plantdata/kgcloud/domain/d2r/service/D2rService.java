package com.plantdata.kgcloud.domain.d2r.service;

import com.plantdata.kgcloud.domain.d2r.entity.PreviewReq;
import com.plantdata.kgcloud.domain.d2r.entity.PreviewRsp;

/**
 * @author xiezhenxiang 2019/12/9
 **/
public interface D2rService {

    PreviewRsp preview(PreviewReq req);
}
