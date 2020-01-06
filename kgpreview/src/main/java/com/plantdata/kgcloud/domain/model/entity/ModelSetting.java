package com.plantdata.kgcloud.domain.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/7/5
 */
@Data
@ApiModel
public class ModelSetting {

    /** 读取方式，0数据库 1接口路径 */
    private Integer readType;
    /** 接口路径 */
    private String path;
    /** 数据库连接类型 0.mysql 1.hive*/
    private Integer dbConnectType;
    /** 数据库 ip */
    private String ip;
    /** 数据库端口 */
    private Integer port;
    /** 数据库账号 */
    private String userName;
    /** 数据库密码 */
    private String password;
    /** 数据库名 */
    private String database;
    private String kgName;
    /** 入图方式 0.仅模式 1.模式+数据 */
    private Integer model;
    /** 实体表映射 */
    private List<EntityTable> entityTables;
    /** 关系表映射 */
    private List<RelationConfig> relationTables;
}
