package ai.plantdata.kgcloud.plantdata.converter.semantic;

import ai.plantdata.kgcloud.plantdata.req.semantic.GremlinParameter;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 14:15
 */
public class GremlinConverter extends BasicConverter {

    public static GremlinReq gremlinParameterToQueryReq(@NonNull GremlinParameter param) {
        GremlinReq req = new GremlinReq();
        req.setGremlin(param.getGremlin());
        return req;
    }
}
