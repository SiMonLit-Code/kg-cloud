package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 16:44
 */
@ApiModel("边属性搜索-参数")
@Getter
@Setter
@NoArgsConstructor
public class EdgeSearchReq extends PageReq {
    @ApiModelProperty("起点id")
    private List<Long> entityIds;
    @ApiModelProperty("属性id")
    private List<Integer> attrIds;
    @ApiModelProperty("属性key")
    private List<String> attrKeys;
    @ApiModelProperty("属性值")
    private List<Long> attrValueIds;
    @ApiModelProperty("开始时间")
    private Map<String,Object> attrTimeFrom;
    @ApiModelProperty("结束时间")
    private Map<String,Object> attrTimeTo;
    @ApiModelProperty("1有向0无向")
    private Integer direction = 0;
    @ApiModelProperty("边属性搜索参数")
    private List<RelationAttrReq> edgeAttrQuery;


}
