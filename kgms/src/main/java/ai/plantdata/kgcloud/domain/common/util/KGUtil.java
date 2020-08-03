package ai.plantdata.kgcloud.domain.common.util;

import ai.plantdata.cloud.web.util.SpringContextUtils;
import ai.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import org.apache.tomcat.util.collections.ConcurrentCache;

import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-19 17:17
 **/
public class KGUtil {

    private final static ConcurrentCache<String, String> map = new ConcurrentCache<>(16);

    public static String dbName(String kgName) {
        return Optional.ofNullable(map.get(kgName)).orElseGet(() -> {
            final String dbName = SpringContextUtils.getBean(GraphService.class).getDbName(kgName);
            map.put(kgName, dbName);
            return kgName;
        });
    }
}
