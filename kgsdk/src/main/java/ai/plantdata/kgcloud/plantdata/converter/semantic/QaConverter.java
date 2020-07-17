package ai.plantdata.kgcloud.plantdata.converter.semantic;

import ai.plantdata.kgcloud.plantdata.req.semantic.QaKbqaParameter;
import ai.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import ai.plantdata.kgcloud.sdk.req.app.sematic.QueryReq;
import lombok.NonNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 14:15
 */
public class QaConverter extends BasicConverter {

    public static QueryReq qaKbqaParameterToQueryReq(@NonNull QaKbqaParameter param) {
        QueryReq queryReq = new QueryReq();
        queryReq.setPos((param.getPageNo() - 1) * param.getPageSize());
        queryReq.setQuery(param.getQuery());
        queryReq.setSize(param.getPageSize());
        return queryReq;
    }
}
