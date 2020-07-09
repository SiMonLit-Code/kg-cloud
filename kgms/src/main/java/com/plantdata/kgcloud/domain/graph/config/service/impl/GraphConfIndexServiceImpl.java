package com.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kg.api.edit.IndexApi;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfIndex;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfIndexRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfIndexService;
import com.plantdata.kgcloud.sdk.rsp.GraphConfIndexStatusRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 17:38
 **/
@Service
public class GraphConfIndexServiceImpl implements GraphConfIndexService {

    @Autowired
    private GraphConfIndexRepository graphConfIndexRepository;

    @Autowired
    private IndexApi indexApi;

    private static String ENAITTY_TABLE = "basic_info";

    private static String ENATITY_UPDATE_DATE_FIELD = "meta_data.meta_data_2";

    @Override
    public GraphConfIndexStatusRsp getStatus(String kgName) {
        GraphConfIndex graphConfIndex = new GraphConfIndex();
        graphConfIndex.setKgName(kgName);
        Optional<GraphConfIndex> optional = graphConfIndexRepository.findOne(Example.of(graphConfIndex));
        if (!optional.isPresent()) {
            graphConfIndex.setStatus(0);
            GraphConfIndex save = graphConfIndexRepository.save(graphConfIndex);
            return ConvertUtils.convert(GraphConfIndexStatusRsp.class).apply(save);
        }
        return ConvertUtils.convert(GraphConfIndexStatusRsp.class).apply(optional.get());
    }

    @Override
    public void updateStatus(String kgName, Integer status) {
        GraphConfIndex graphConfIndex = new GraphConfIndex();
        graphConfIndex.setKgName(kgName);
        Optional<GraphConfIndex> optional = graphConfIndexRepository.findOne(Example.of(graphConfIndex));
        if (!optional.isPresent()) {
            graphConfIndex.setStatus(status);
            graphConfIndexRepository.save(graphConfIndex);
        }
        GraphConfIndex newGraphConfIndexStatus = optional.get();
        newGraphConfIndexStatus.setStatus(status);
        graphConfIndexRepository.save(newGraphConfIndexStatus);

        indexApi.create(KGUtil.dbName(kgName),ENAITTY_TABLE,ENATITY_UPDATE_DATE_FIELD);
    }
}
