package ai.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.dictionary.entity.Dictionary;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfSnapshot;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfSnapshotRepository;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfSnapshotService;
import ai.plantdata.kgcloud.sdk.req.GraphConfSnapshotReq;
import ai.plantdata.kgcloud.sdk.req.app.PageReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfSnapshotRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /**
     * 保存快照
     * @param graphConfSnapshotReq
     */
    @Override
    public GraphConfSnapshotRsp saveSnapShot(GraphConfSnapshotReq graphConfSnapshotReq) {
        GraphConfSnapshot graphConfSnapshot = ConvertUtils.convert(GraphConfSnapshot.class).apply(graphConfSnapshotReq);
        graphConfSnapshot.setId(kgKeyGenerator.getNextId());
        GraphConfSnapshot save = graphConfSnapshotRepository.save(graphConfSnapshot);
        return ConvertUtils.convert(GraphConfSnapshotRsp.class).apply(save);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deleteSnapShot(Long id) {
        graphConfSnapshotRepository.deleteById(id);
    }

    /**
     * 查询所有-分页
     * @param p
     * @return
     */
    @Override
    public Page<GraphConfSnapshotRsp> findAllSnapShot(String kgName,String spaId,BaseReq p) {
        GraphConfSnapshot graphConfSnapshot=new GraphConfSnapshot();
        graphConfSnapshot.setKgName(kgName);
        graphConfSnapshot.setSpaId(spaId);
        return graphConfSnapshotRepository.findAll(Example.of(graphConfSnapshot),PageRequest.of(p.getPage()-1,p.getSize()))
                .map(ConvertUtils.convert(GraphConfSnapshotRsp.class));
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @Override
    public GraphConfSnapshotRsp findByIdSnapShot(Long id) {
        Optional<GraphConfSnapshot> optional = graphConfSnapshotRepository.findById(id);
        GraphConfSnapshot graphConfSnapshot = optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_ID_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfSnapshotRsp.class).apply(graphConfSnapshot);
    }
}
