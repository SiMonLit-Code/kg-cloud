package com.plantdata.kgcloud.domain.j2r.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xiezhenxiang 2019/12/17
 */
@Data
@ApiModel
public class PathPreviewReq {

    @ApiModelProperty("json字符串")
    private String jsonStr;
    @ApiModelProperty("jsonPath数组")
    private List<String> jsonPaths;
}
