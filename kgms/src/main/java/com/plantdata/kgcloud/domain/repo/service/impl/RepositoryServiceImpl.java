package com.plantdata.kgcloud.domain.repo.service.impl;

import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.converter.RepositoryConverter;
import com.plantdata.kgcloud.domain.repo.factory.ServiceCheckerFactory;
import com.plantdata.kgcloud.domain.repo.model.Repository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryUseLog;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryRepository;
import com.plantdata.kgcloud.domain.repo.repository.RepositoryUseLogRepository;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/18  16:03
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryRepository repositoryRepository;
    @Autowired
    private RepositoryUseLogRepository repositoryUseLogRepository;

    @Override
    public List<RepositoryRsp> list(String userId) {
        List<Repository> all = repositoryRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyList();
        }
        List<RepositoryRsp> repositoryRspList = BasicConverter.listToRsp(all, RepositoryConverter::repository2RepositoryRsp);
        //填充组件状态
        Function<Repository, Boolean> health = b -> ServiceCheckerFactory.factory(b.getCheckConfigs()).stream().allMatch(ServiceChecker::check);
        Map<Integer, Boolean> stateMap = all.stream().collect(Collectors.toMap(Repository::getId, health));
        BasicConverter.listConsumerIfNoNull(repositoryRspList, a -> a.setEnable(stateMap.get(a.getId())));
        //检测是否为最新
        List<RepositoryUseLog> useLogs = repositoryUseLogRepository.findAllByUserIdAndRepositoryIdIn(userId, stateMap.keySet());
        BasicConverter.consumerIfNoNull(useLogs, a -> {
            Set<Integer> repositoryIds = useLogs.stream().map(RepositoryUseLog::getRepositoryId).collect(Collectors.toSet());
            repositoryRspList.forEach(b -> b.setNewFunction(repositoryIds.contains(b.getId())));
        });
        return repositoryRspList;
    }

    @Override
    public Integer add(RepositoryReq repositoryReq) {
        Repository repository = repositoryRepository.save(RepositoryConverter.repositoryReq2Repository(repositoryReq));
        return repository.getId();
    }

    @Override
    public boolean updateStatus(Integer id, boolean start) {
        Optional<Repository> repOpt = repositoryRepository.findById(id);
        repOpt.ifPresent(a -> {
            boolean res = ServiceCheckerFactory.factory(a.getCheckConfigs()).stream().anyMatch(ServiceChecker::check);
            if (res) {
                throw new BizException("组件启用失败,健康检测未通过");
            }
            a.setState(start);
            repositoryRepository.save(a);
            //清楚使用状态
            repositoryUseLogRepository.deleteByRepositoryId(id);
        });
        return true;
    }

    @Override
    public void useLog(Integer id, String userId) {
        Optional<Repository> repOpt = repositoryRepository.findById(id);
        repOpt.ifPresent(a -> {
            RepositoryUseLog useLog = repositoryUseLogRepository.findByUserIdAndRepositoryId(userId, id);
            if (useLog == null) {
                repositoryUseLogRepository.save(new RepositoryUseLog(a.getId(), a.getMenuId(), userId));
            }
        });
    }
}
