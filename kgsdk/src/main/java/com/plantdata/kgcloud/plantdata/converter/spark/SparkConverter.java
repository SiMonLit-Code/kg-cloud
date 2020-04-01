package com.plantdata.kgcloud.plantdata.converter.spark;

import com.plantdata.kgcloud.plantdata.req.app.SparqlQueryParameter;
import com.plantdata.kgcloud.sdk.req.app.SparQlReq;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/4 18:18
 */
public class SparkConverter {

    public static SparQlReq sparqlQueryParameterToSparQlReq(SparqlQueryParameter param) {
        SparQlReq sparQlReq = new SparQlReq();
        sparQlReq.setQuery(param.getQuery());
        sparQlReq.setSize(param.getSize());
        return sparQlReq;
    }
}
