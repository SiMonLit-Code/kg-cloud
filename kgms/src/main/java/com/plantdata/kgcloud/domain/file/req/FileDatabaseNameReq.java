package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:46
 */
@Data
@ApiModel("数据库改名参数")
public class FileDatabaseNameReq {

    private Long databaseId;

    private String name;

}
