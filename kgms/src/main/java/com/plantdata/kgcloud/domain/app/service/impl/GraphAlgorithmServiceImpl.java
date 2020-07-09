package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.exception.BizException;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.service.GraphAlgorithmService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.app.util.SseUtils;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 9:51
 */
@Service
@Slf4j
public class GraphAlgorithmServiceImpl implements GraphAlgorithmService {

    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    @Override
    public BusinessGraphRsp run(String kgName, Long id, BusinessGraphRsp graphBean) {
        GraphConfAlgorithmRsp confAlgorithmRsp = graphConfAlgorithmService.findById(id);
        if(confAlgorithmRsp.getType() != 1){
            throw BizException.of(AppErrorCodeEnum.ALGORITHM_TYPE_ERROR);
        }
        if (confAlgorithmRsp != null) {
            String url = confAlgorithmRsp.getAlgorithmUrl();
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("kgName", kgName);
            form.add("graphBean", JsonUtils.objToJson(graphBean));
            try {
                return SseUtils.postForObject(url, null, form, BusinessGraphRsp.class);
            } catch (Exception e) {
                log.error("url:{},message:{}", url, e.getMessage());
                throw BizException.of(AppErrorCodeEnum.ALGORITHM_EXECUTE_ERROR);
            }
        }
        return graphBean;
    }

    @Override
    public AlgorithmStatisticeRsp runStatistics(String kgName, long id, BusinessGraphRsp graphBean) {
        GraphConfAlgorithmRsp confAlgorithmRsp = graphConfAlgorithmService.findById(id);

        if(confAlgorithmRsp.getType() != 2){
            throw BizException.of(AppErrorCodeEnum.ALGORITHM_TYPE_ERROR);
        }
        if (confAlgorithmRsp != null) {
            String url = confAlgorithmRsp.getAlgorithmUrl();
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("kgName", kgName);
            form.add("graphBean", JsonUtils.objToJson(graphBean));
            try {
                return SseUtils.postForObject(url, null, form, AlgorithmStatisticeRsp.class);
            } catch (Exception e) {
                log.error("url:{},message:{}", url, e.getMessage());
                throw BizException.of(AppErrorCodeEnum.ALGORITHM_EXECUTE_ERROR);
            }
        }else{
            throw BizException.of(AppErrorCodeEnum.ALGORITHM_EXECUTE_ERROR);
        }

    }

}
