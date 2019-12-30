package com.plantdata.kgcloud.config;

import com.mongodb.MongoClient;
import com.plantdata.kgcloud.common.util.DriverUtil;
import com.plantdata.kgcloud.common.util.EsRestUtil;
import com.plantdata.kgcloud.common.util.MongoUtil;
import com.plantdata.kgcloud.common.util.SpringBeanUtil;
import org.elasticsearch.client.RestClient;

/**
 * @author xiezhenxiang 2019/12/30
 */
public class Constants {

    public static final MongoUtil KG_MONGO = new MongoUtil(SpringBeanUtil.getBean("mongoClient", MongoClient.class));
    public static final EsRestUtil KG_ES = EsRestUtil.getInstance(SpringBeanUtil.getBean("restClient", RestClient.class));
    private static final AppConfig appConfig = SpringBeanUtil.getBean(AppConfig.class);
    public static final DriverUtil KG_MYSQL = DriverUtil.getInstance(appConfig.dbUrl, appConfig.dbUser, appConfig.dbPwd);
}
