package ai.plantdata.kgcloud.domain.common.util;

import ai.plantdata.cloud.web.util.SpringContextUtils;
import ai.plantdata.kgcloud.domain.graph.manage.service.GraphService;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-19 17:17
 **/
public class KGUtil {

    public static String dbName(String kgName) {
        return SpringContextUtils.getBean(GraphService.class).getDbName(kgName);
    }
}
