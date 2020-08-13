package ai.plantdata.kgcloud.domain.graph.config.service;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfSnapshot;
import ai.plantdata.kgcloud.sdk.req.GraphConfSnapshotReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfSnapshotRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 16:48
 **/
public interface GraphConfSnapshotService {
    /**
     * 保存快照
     * @param graphConfSnapshot
     */
    GraphConfSnapshotRsp saveSnapShot(GraphConfSnapshotReq graphConfSnapshot);

    /**
     * 删除快照
     * @param id
     */
    void deleteSnapShot(Long id);

    /**
     * 查询所有快照
     * @return
     */
    Page<GraphConfSnapshotRsp> findAllSnapShot(String kgName,String spaId,BaseReq p);

    /**
     * 按照id查询
     * @param id
     * @return
     */
    GraphConfSnapshotRsp findByIdSnapShot(Long id);

}
