package com.plantdata.kgcloud.domain.model.service;

import com.plantdata.kgcloud.domain.model.entity.ModelSetting;
import com.plantdata.kgcloud.domain.model.entity.TableSchemaRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author xiezhenxiang 2019/12/10
 */
public interface ModelService {

    void dbTest(String path, Integer type, String ip, Integer port, String database, String userName, String pwd);

    Page<String> getTables(String path, Integer type, String ip, Integer port, String database, String userName, String pwd, String kw, Integer pageNo, Integer pageSize);

    List<TableSchemaRsp> getTableSchema(String path, Integer type, String ip, Integer port, String database, String userName, String pwd, List<String> ls);

    void testConfig(ModelSetting set);
}
