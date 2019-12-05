package com.plantdata.kgcloud.sdk.rsp.app.main;

import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 10:11
 */
@ApiModel("高级实体查询视图")
@Getter
@Setter
public class SeniorPromptRsp extends PromptEntityRsp {

    @ApiModelProperty("图片")
    private ImageRsp img;
    private String abs;
    private List<String> synonyms;
}
