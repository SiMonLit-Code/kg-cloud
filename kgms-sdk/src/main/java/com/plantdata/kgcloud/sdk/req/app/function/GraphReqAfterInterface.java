package com.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 10:01
 */
public interface GraphReqAfterInterface {

    /**
     * 要替换的classId
     *
     * @return 。
     */
    List<Long> getReplaceClassIds();

    /**
     * 是否进行关系合并
     *
     * @return 。
     */
    boolean isRelationMerge();
}
