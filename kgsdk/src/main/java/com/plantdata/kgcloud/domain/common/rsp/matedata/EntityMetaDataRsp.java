package com.plantdata.kgcloud.domain.common.rsp.matedata;

import com.plantdata.kgcloud.domain.app.rsp.GisRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 */
@Getter
@Setter
@ApiModel("元数据")
public class EntityMetaDataRsp {
    private Double score;
    private List<TagRsp> tags;
    @ApiModelProperty("gis信息")
    private List<EntityLinkRsp> entityLinks;
    private String fromTime;
    private String toTime;
    private String batch;
    private String source;
    private Double reliability;
    @ApiModelProperty("gis信息")
    private GisRsp gis;
    @ApiModelProperty("创建时间")
    private String creationTime;
    @ApiModelProperty("样式信息")
    private AdditionalRsp additionalInfo;
    @ApiModelProperty("私有属性")
    private Map<String, Object> extra;
}
