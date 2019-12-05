package com.plantdata.kgcloud.domain.data.obtain.req;


import com.plantdata.kgcloud.domain.data.statistics.req.RelationByFilterReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author cjw 2019-11-01 15:46:19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("关系度数统计")
public class CountRelationByEntityReq {
    @ApiModelProperty("是否去重 true 去重")
    private Boolean isDistinct = false;
    @ApiModelProperty("查询指定的属性，格式[{'layer':'层数';'ids':[查询的id列表]}]，默认为查询全部")
    private List<RelationByFilterReq<Integer>> allowAttrs;
    @ApiModelProperty("查询指定的概念，格式[{'layer':'层数';'ids':[查询的id列表]}]，默认为查询全部")
    private List<RelationByFilterReq> allowTypes;


}
