package com.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/17 17:27
 */
public interface ConceptKeyListReqInterface {


    List<Long> getAllowConcepts();

    void setAllowConcepts(List<Long> allowConceptIds);

    List<String> getAllowConceptsKey();
}
