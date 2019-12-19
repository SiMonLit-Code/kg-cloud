package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 9:54
 */
public class ApkConverter extends BasicConverter {

    public static ApkRsp graphRspToApkRsp(GraphRsp graphRsp, String apk) {
        return new ApkRsp(graphRsp.getKgName(), graphRsp.getTitle(), apk);
    }
}
