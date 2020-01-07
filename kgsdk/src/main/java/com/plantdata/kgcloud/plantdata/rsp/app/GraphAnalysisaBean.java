package com.plantdata.kgcloud.plantdata.rsp.app;

import com.plantdata.kgcloud.plantdata.req.common.RelationBean;
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
public class GraphAnalysisaBean {

	private List<EntityBeanAnalysisa> entityList;
	private List<RelationBean> relationList;
	private List<PathAGBean> connects;
	private List<GraphStatBean> stats;
	private Integer level1HasNextPage;

}
