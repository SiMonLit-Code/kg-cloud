package com.plantdata.kgcloud.domain.common.service;

import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;

public interface SDKService {
    Long getEntityIdByName(String kgName, String name, Long conceptId);

    InfoBoxRsp infobox(String kgName, Long entityId);

}
