package ai.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.graph.config.constant.FocusType;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfFocusRepository;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfFocusService;
import ai.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiangdeming
 * @date 2019/11/29
 */
@Service
public class GraphConfFocusServiceImpl implements GraphConfFocusService {

    @Autowired
    GraphConfFocusRepository graphConfFocusRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfFocusRsp> save(String kgName, List<GraphConfFocusReq> reqs) {
        List<GraphConfFocus> list = new ArrayList<>();
        for (GraphConfFocusReq req : reqs) {
            GraphConfFocus targe = new GraphConfFocus();
            BeanUtils.copyProperties(req, targe);
            if (!FocusType.contains(req.getType())) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_FOCUS_ERROR);
            }
            FocusType.check(req.getEntities().size(), req.getType());
            targe.setKgName(kgName);
            String code = FocusType.findType(req.getType()).getCode();
            targe.setType(code);
            list.add(targe);
        }
        List<GraphConfFocus> result = graphConfFocusRepository.saveAll(list);
        return result.stream().map(ConvertUtils.convert(GraphConfFocusRsp.class)).collect(Collectors.toList());
    }

    @Override
    public List<GraphConfFocusRsp> findByKgName(String kgName) {
        Optional<List<GraphConfFocus>> result = graphConfFocusRepository.findByKgName(kgName);
        List<GraphConfFocus> list = result.orElseGet(() -> {
            ArrayList<GraphConfFocus> resultList = new ArrayList<>();
            for (FocusType focusType : FocusType.values()) {
                GraphConfFocus e = new GraphConfFocus();
                e.setType(focusType.getCode());
                e.setEntities(JacksonUtils.getInstance().createArrayNode());
                resultList.add(e);
            }
            return resultList;
        });
        Set<String> resultTypeSet = list.stream().map(GraphConfFocus::getType).collect(Collectors.toSet());
        for (FocusType focusType : FocusType.values()) {
            if (!resultTypeSet.contains(focusType.getCode())) {
                GraphConfFocus e = new GraphConfFocus();
                e.setType(focusType.getCode());
                e.setEntities(JacksonUtils.getInstance().createArrayNode());
                list.add(e);
            }
        }
        return list.stream().map(ConvertUtils.convert(GraphConfFocusRsp.class)).collect(Collectors.toList());
    }
}
