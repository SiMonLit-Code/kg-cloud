package com.plantdata.kgcloud.sdk.req.app.algorithm;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("复杂图算法参数")
public class BusinessGraphRsp {
    private List<BusinessEntityBean> entityList;
    private List<BusinessRelationBean> relationList;
    private List<PathAGBean> connects;
    private List<GraphStatBean> stats;
    private Integer level1HasNextPage;
    private String message;
    private String layout;

}
