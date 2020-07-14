package ai.plantdata.kgcloud.domain.repo.checker;

import ai.plantdata.cloud.web.util.SpringContextUtils;
import com.mongodb.MongoClient;
import ai.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  11:12
 */
@Slf4j
public class MongoServiceChecker implements ServiceChecker {

    private static Function<String, Boolean> graphExistFunction = a -> {
        boolean flag = false;
        for (String listDatabaseName : SpringContextUtils.getBean(MongoClient.class).listDatabaseNames()) {
            if (listDatabaseName.equals(a)) {
                flag = true;
                break;
            }
        }
        return flag;
    };
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
