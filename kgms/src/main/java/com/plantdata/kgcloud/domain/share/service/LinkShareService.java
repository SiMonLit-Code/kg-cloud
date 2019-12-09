package com.plantdata.kgcloud.domain.share.service;

import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;

/**
 * Created by plantdata-1007 on 2019/12/714:25.
 */
public interface LinkShareService {

    LinkShareRsp status(String userId, String kgName);

    Boolean shareLink(String userId, String kgName, String spaId);

    Boolean cancelShare(String userId, String kgName,String spaId);

}
