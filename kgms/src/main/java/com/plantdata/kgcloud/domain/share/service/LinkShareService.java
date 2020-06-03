package com.plantdata.kgcloud.domain.share.service;

import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;

import com.plantdata.kgcloud.sdk.req.SelfSharedRsp;
import com.plantdata.kgcloud.sdk.rsp.LinkShareSpaRsp;
import com.plantdata.kgcloud.domain.share.rsp.ShareRsp;

/**
 * Created by plantdata-1007 on 2019/12/714:25.
 */
public interface LinkShareService {

    LinkShareSpaRsp shareStatus(String userId, String kgName, String spaId);

    LinkShareRsp shareStatus(String userId, String kgName);

    LinkShareRsp liteShareStatus(String userId);

    ShareRsp shareLink(String userId, String kgName, String spaId);

    ShareRsp shareCancel(String userId, String kgName, String spaId);

    SelfSharedRsp shareSpaStatus(String userId, String kgName, String spaId, String token);

    void refresh();
}
