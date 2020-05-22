package com.plantdata.kgcloud.domain.repo.checker;

import com.mongodb.MongoClient;
import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import com.plantdata.kgcloud.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  11:12
 */
@Slf4j
public class MongoServiceChecker implements ServiceChecker {

    private static Function<String, Boolean> graphExistFunction = a -> SpringContextUtils.getBean(MongoClient.class).getDatabase(a) == null;
    private List<RepoCheckConfig> checkConfigs;

    public MongoServiceChecker(List<RepoCheckConfig> checkConfigs) {
        this.checkConfigs = checkConfigs;
    }

    @Override
    public boolean check() {
        boolean match = checkConfigs.stream().allMatch(a -> graphExistFunction.apply(a.getContent()));
        if (!match) {
            log.error("检测失败,图谱不存在");
            return false;
        }
        return true;
    }
}
