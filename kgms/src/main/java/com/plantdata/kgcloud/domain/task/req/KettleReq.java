package com.plantdata.kgcloud.domain.task.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 14:10
 **/
@Data
@ApiModel
public class KettleReq {

    @ApiParam(value = "图谱名称")
    private String kgName;

    @ApiParam(value = "任务名称")
    private String taskName;

    @ApiParam(value = "连接类型")
    private String connectType;

    @ApiParam(value = "数据库名称")
    private String database;

    @ApiParam(value = "主机地址")
    private String host;

    @ApiParam(value = "端口")
    private String port;

    @ApiParam(value = "用户名")
    private String username;

    @ApiParam(value = "密码")
    private String password;

    @ApiParam(value = "执行sql")
    private String sql;

    @ApiParam(hidden = true)
    private List<String> mongoAddress;

    @ApiParam(value = "mongoDbName", hidden = true)
    private String mongoDbName;

    @ApiParam(value = "mongoTbName", hidden = true)
    private String mongoTbName;
}
