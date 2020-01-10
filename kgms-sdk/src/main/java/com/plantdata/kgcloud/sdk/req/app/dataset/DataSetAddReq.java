package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("数据集数据新增-参数")
public class DataSetAddReq {
    @NotNull
    @ApiModelProperty("数据[{\"key\":\"value\"}]")
    private List<Map<String, Object>> dataList;
    @NotBlank
    @ApiModelProperty("数据集唯一标识")
    private String dataSetId;

}
