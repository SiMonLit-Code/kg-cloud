package com.plantdata.kgcloud.domain.share.rsp;

import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@ApiModel("图谱分享模型")
public class LinkShareRsp {

    @ApiModelProperty(value = "是否分享")
    private Integer hasRole;

    @ApiModelProperty(value = "分享列表")
    private List<LinkShare> shareList;
}
