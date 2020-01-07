package com.plantdata.kgcloud.plantdata.req.rule;

import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import com.plantdata.kgcloud.plantdata.bean.BusinessEntityBean;
import com.plantdata.kgcloud.plantdata.bean.BusinessRelationBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.PathAGBean;
import lombok.*;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BusinessGraphBean  implements MarkObject {
    private List<BusinessEntityBean> entityList;
    private List<BusinessRelationBean> relationList;
    private List<PathAGBean> connects;
    private List<GraphStatBean> stats;
    private Integer level1HasNextPage;
    private String message;
    private String layout;

}
