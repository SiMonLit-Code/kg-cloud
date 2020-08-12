package ai.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfSnapshotRepository;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfSnapshotService;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfSnapshotRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bovin
 * @description
 * @since 2020-08-12 16:48
 **/
@Service
public class GraphConfSnapshotServiceImpl implements GraphConfSnapshotService {

    @Autowired
    private GraphConfSnapshotRepository graphConfSnapshotRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


}
