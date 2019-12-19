package com.plantdata.kgcloud.domain.common.util;

import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.util.SpringContextUtils;

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
