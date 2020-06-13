package com.plantdata.kgcloud.plantdata.converter.semantic;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.semantic.GremlinParameter;
import com.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import com.plantdata.kgcloud.sdk.req.app.sematic.GremlinReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
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
