package ai.plantdata.kgcloud.config;

import com.mongodb.MongoClient;
import ai.plantdata.kgcloud.common.util.DriverUtil;
import ai.plantdata.kgcloud.common.util.MongoUtil;
import ai.plantdata.kgcloud.common.util.SpringBeanUtil;

/**
 * @author xiezhenxiang 2019/12/30
 */
public class Constants {

    public static final MongoUtil KG_MONGO = new MongoUtil(SpringBeanUtil.getBean("mongoClient", MongoClient.class));
    private static final AppConfig appConfig = SpringBeanUtil.getBean(AppConfig.class);
    public static final DriverUtil KG_MYSQL = DriverUtil.getInstance(appConfig.dbUrl, appConfig.dbUser, appConfig.dbPwd);
}
