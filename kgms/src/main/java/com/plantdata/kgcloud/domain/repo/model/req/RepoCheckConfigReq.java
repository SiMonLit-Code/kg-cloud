package com.plantdata.kgcloud.domain.repo.model.req;

import com.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/20  10:45
 */
@Getter
@Setter
@ApiModel("repo检测配置参数")
public class RepoCheckConfigReq {
    @ApiModelProperty(value = "检测类型", required = true)
    private RepoCheckType checkType;
    @ApiModelProperty(value = "检测内容 服务名称/文件名称/图谱名称", required = true)
    private String content;
}
