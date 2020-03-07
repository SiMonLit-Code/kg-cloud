package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2020/3/7 23:55
 * @Description:
 */
@Data
@ApiModel("实体关系列表查询")
public class EntityAttrReq {
    @NotNull
    private Long conceptId;
    @NotNull
    private Long entityId;
    private Integer page = 0;
    private Integer size = 10;
}
