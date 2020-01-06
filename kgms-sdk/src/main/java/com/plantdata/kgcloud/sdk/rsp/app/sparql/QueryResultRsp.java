package com.plantdata.kgcloud.sdk.rsp.app.sparql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QueryResultRsp {
    private int count;
    private List<List<SparQlNodeRsp>> result;
}
