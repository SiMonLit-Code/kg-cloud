package ai.plantdata.kgcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiezhenxiang 2019/12/30
 */
@Component
public class AppConfig {

    @Value("${db.kgms}")
    public String dbUrl;
    @Value("${spring.datasource.username}")
    public String dbUser;
    @Value("${spring.datasource.password}")
    public String dbPwd;
}
