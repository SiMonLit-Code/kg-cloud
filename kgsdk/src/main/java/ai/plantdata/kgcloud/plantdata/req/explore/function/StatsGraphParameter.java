package ai.plantdata.kgcloud.plantdata.req.explore.function;


import ai.plantdata.kgcloud.plantdata.req.explore.common.GraphStatBean;

import java.util.List;

public interface StatsGraphParameter {

    List<GraphStatBean> getStatsConfig();

    void setStatsConfig(List<GraphStatBean> statsConfig);
}
