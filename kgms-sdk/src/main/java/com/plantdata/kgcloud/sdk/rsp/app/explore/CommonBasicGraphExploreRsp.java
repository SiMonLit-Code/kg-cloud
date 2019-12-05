package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 16:39
 */
@ToString
@Getter
@Setter
@ApiModel("图探索视图")
@NoArgsConstructor
public class CommonBasicGraphExploreRsp extends BasicGraphExploreRsp {

    @ApiModelProperty("实例或者概念")
    private List<CommonEntityRsp> entityList;

    public CommonBasicGraphExploreRsp(List<BasicRelationRsp> relationList, Integer hasNextPage, List<CommonEntityRsp> entityList) {
        super(relationList, hasNextPage);
        this.entityList = entityList;
    }

    public static final CommonBasicGraphExploreRsp EMPTY = new CommonBasicGraphExploreRsp(Collections.emptyList(), NumberUtils.INTEGER_ZERO, Collections.emptyList());
}
