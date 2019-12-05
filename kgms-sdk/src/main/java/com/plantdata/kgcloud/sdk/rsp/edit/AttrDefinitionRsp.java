package com.plantdata.kgcloud.sdk.rsp.edit;

import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 11:46
 * @Description:
 */
@Data
@ApiModel("属性定义查询结果模型")
public class AttrDefinitionRsp extends AttrDefinitionVO {

    @ApiModelProperty(value = "属性定义域概念名称")
    private String domainName;

    @ApiModelProperty(value = "值域概念id和名称")
    private List<KVBean<Long, String>> rangeNameValue;
}
