package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:39
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicGraphExploreRsp {
    @ApiModelProperty("关系")
    private List<BasicRelationRsp> relationList;
    @ApiModelProperty("是否有下一页 1 有 0无")
    private Integer hasNextPage;
}
