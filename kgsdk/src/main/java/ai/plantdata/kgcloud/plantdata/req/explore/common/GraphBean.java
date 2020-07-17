package ai.plantdata.kgcloud.plantdata.req.explore.common;

import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
import ai.plantdata.kgcloud.plantdata.req.common.RelationBean;
import ai.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GraphBean implements MarkObject {

    private List<EntityBean> entityList;
    private List<RelationBean> relationList;
    private List<PathAGBean> connects;
    private List<GraphStatBean> stats;
    private Integer level1HasNextPage;

}
