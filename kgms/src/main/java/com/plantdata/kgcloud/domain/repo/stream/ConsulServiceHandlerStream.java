package com.plantdata.kgcloud.domain.repo.stream;

import com.alibaba.excel.util.CollectionUtils;
import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import com.plantdata.kgcloud.domain.repo.model.ConsulService;
import com.plantdata.kgcloud.domain.repo.model.RepositoryHandler;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsulServiceHandlerStream implements HandlerStream {

    private RepositoryRoot repositoryRoot;

    public ConsulServiceHandlerStream(RepositoryRoot repositoryRoot) {
        this.repositoryRoot = repositoryRoot;
    }

    @Override
    public Object handler() {
        assert repositoryRoot instanceof ConsulService;
        List<RepositoryHandler> handlers = ((ConsulService) repositoryRoot).handlers();
        Map<HandleType, List<RepositoryHandler>> handleTypeListMap = handlers.stream().collect(Collectors.groupingBy(RepositoryHandler::getHandleType));
        Object beforeRsp = exec(handleTypeListMap, HandleType.BEFORE, repositoryRoot.getBasicReq());
        Object basicRsp = repositoryRoot.BasicRequest().apply(beforeRsp);
        return  exec(handleTypeListMap, HandleType.AFTER, basicRsp);
    }

    private Object exec(Map<HandleType, List<RepositoryHandler>> handleTypeListMap, HandleType handleType, Object param) {
        List<RepositoryHandler> before = handleTypeListMap.get(handleType);
        if (!CollectionUtils.isEmpty(before)) {
            for (RepositoryHandler handler : before) {
              try {
                  param = handler.handler(param);
              }catch (Exception e){
                  e.printStackTrace();
              }
            }
        }
        return param;
    }
}