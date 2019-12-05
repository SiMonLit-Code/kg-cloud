package com.plantdata.kgcloud.sdk.rsp.app.main;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/11 14:24
 */
@Getter
@Setter
@ApiModel("知识卡片实体视图")
public class InfoBoxEntityRsp extends BasicEntityRsp {

    @ApiModelProperty("标签信息")
    private List<TagRsp> tags;
    @ApiModelProperty("数据集关联")
    private List<DataSetLinkRsp> dataLinks;
    @ApiModelProperty("实体关联")
    private List<EntityLinkRsp> entityLinks;
    @ApiModelProperty("私有属性")
    private List<Map<String, Object>> extra;
}
