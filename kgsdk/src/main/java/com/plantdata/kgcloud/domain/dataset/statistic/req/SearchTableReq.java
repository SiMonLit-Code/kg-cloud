package com.plantdata.kgcloud.domain.dataset.statistic.req;

import com.plantdata.kgcloud.sdk.req.app.dataset.BaseTableReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author cjw 2019-11-07 14:12:26
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("搜索数据集查询参数")
public class SearchTableReq extends BaseTableReq {
    @ApiModelProperty("es index")
    private List<String> databases;
    @ApiModelProperty("es type")
    private List<String> tables;

}
