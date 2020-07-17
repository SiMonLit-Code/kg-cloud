package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import ai.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 9:54
 */
public class ApkConverter extends BasicConverter {

    public static ApkRsp graphRspToApkRsp(Graph graph, String apk) {
        return new ApkRsp(graph.getKgName(), graph.getTitle(), apk);
    }
}
