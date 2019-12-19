package com.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 10:01
 */
public interface GraphReqAfterInterface {


    List<Long> getReplaceClassIds();

    boolean isRelationMerge();
}
