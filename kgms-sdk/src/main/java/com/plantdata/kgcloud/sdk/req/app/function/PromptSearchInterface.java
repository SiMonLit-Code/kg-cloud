package com.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 17:09
 */
public interface PromptSearchInterface {

    String getKw();

    List<Long> getConceptIds();

    Boolean getInherit();

    int getOffset();

    int getLimit();
}
