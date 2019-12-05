package com.plantdata.kgcloud.sdk.rsp.app.main;

import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author cjw 2019-11-01 15:17:54
 */
@Getter
@Setter
@ApiModel
public class InfoBoxRsp {
    @ApiModelProperty("基本属性")
    private EntityLinksRsp self;
    @ApiModelProperty("父节点")
    private List<InfoBoxEntityRsp> parents;
    @ApiModelProperty("字节点")
    private List<InfoBoxEntityRsp> sons;
    @ApiModelProperty("正向对象属性")
    private List<InfoBoxAttrRsp> attrs;
    @ApiModelProperty("反向对象属性")
    private List<InfoBoxAttrRsp> reAttrs;

    @Getter
    @Setter
    @ApiModel
    public static class InfoBoxAttrRsp extends ObjectAttributeRsp {
        @ApiModelProperty("属性定义名称")
        private String attrDefName;
    }
}