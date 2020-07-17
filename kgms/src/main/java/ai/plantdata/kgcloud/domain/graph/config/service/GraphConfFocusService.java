package ai.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;

import java.util.List;

/**
 * @author jiangdeming
 * @date 2019/11/29
 */
public interface GraphConfFocusService {

    /**
     * 保存图谱焦点
     *
     * @param kgName
     * @param reqs
     * @return
     */
    List<GraphConfFocusRsp> save(String kgName, List<GraphConfFocusReq> reqs);

    /**
     * 图谱名字查询所有
     *
     * @param kgName
     * @return
     */
    List<GraphConfFocusRsp> findByKgName(String kgName);
}
