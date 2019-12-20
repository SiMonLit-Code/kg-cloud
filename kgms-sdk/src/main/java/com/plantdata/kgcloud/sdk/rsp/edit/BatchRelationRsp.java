package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:46
 * @Description:
 */
@Data
@ApiModel("批量关系新建结果模型")
public class BatchRelationRsp {
    @ApiModelProperty("属性定义id")
    private Integer attrId;
    @ApiModelProperty("实体id")
    private Long entityId;
    @ApiModelProperty("实体名称")
    private String entityName;
    @ApiModelProperty("实体消歧标识")
    private String entityMeaningTag;
    @ApiModelProperty("实体概念")
    private Long entityConcept;
    private Long attrValueId;
    private String attrValueName;
    private String attrValueMeaningTag;
    private Long attrValueConcept;
    private String attrTimeFrom;
    private String attrTimeTo;
    private Map<Integer, String> extraInfoMap;
    private String id;
    private String note;
}
