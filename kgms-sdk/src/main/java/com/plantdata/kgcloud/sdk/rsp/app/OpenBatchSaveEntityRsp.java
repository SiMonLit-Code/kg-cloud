package com.plantdata.kgcloud.sdk.rsp.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 11:57
 */
@Getter
@Setter
@ToString
@ApiModel("新增或修改实体(open)")
public class OpenBatchSaveEntityRsp {
    @ApiModelProperty(value = "概念id", required = true)
    private Long conceptId;
    @ApiModelProperty(value = "实体名称", required = true)
    private String name;
    @ApiModelProperty("消歧标识")
    private String meaningTag;
    private String abs;
    @ApiModelProperty("图片地址")
    private String imageUrl;
    @ApiModelProperty("实体id更新是必填")
    private Long id;
    private String note;
    @ApiModelProperty("同义词")
    private List<String> synonyms;
    @ApiModelProperty("属性")
    private Map<Integer, String> attributes;
    @ApiModelProperty("私有属性")
    private Map<String, String> privateAttributes;
    @ApiModelProperty("元数据")
    private MetaDataReq metaData;

    @ApiModel("元数据")
    @Getter
    @Setter
    public static class MetaDataReq {
        @ApiModelProperty("批次号")
        private String batchNo;
    }
}
