package com.plantdata.kgcloud.domain.model.entity;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiezhenxiang 2019/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TableNameReq extends BaseReq {

    @ApiModelProperty("接口路径") String path;
    @ApiModelProperty("连接类型 0:mysql,1:hive") Integer type;
    @ApiModelProperty("主机地址") String ip;
    @ApiModelProperty("端口") Integer port;
    @ApiModelProperty("数据库名") String database;
    @ApiModelProperty("用户名") String userName;
    @ApiModelProperty("密码") String pwd;
    @ApiModelProperty("关键词") String kw;
}
