package com.plantdata.kgcloud.domain.app.req;

import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author cjw 2019-11-01 14:54:31
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("批量获取知识卡片参数")
public class BatchInfoBoxReq extends InfoBoxReq {

    @ApiModelProperty("实体id")
    private List<Long> ids;

}
