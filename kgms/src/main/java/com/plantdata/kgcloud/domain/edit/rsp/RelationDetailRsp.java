package com.plantdata.kgcloud.domain.edit.rsp;


import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kg.common.bean.ExtraInfo;
import com.plantdata.kgcloud.domain.edit.vo.EntityAttrValueVO;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.domain.edit.vo.GisVO;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrGroupRsp;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:31
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("关系详情")
public class RelationDetailRsp{
    @ApiModelProperty(value = "")
    private List<ExtraInfo> extraInfo;

    @ApiModelProperty(value = "")
    private Map<Integer, Object> relationDataValues;

    @ApiModelProperty(value = "")
    private Map<Integer, List<BasicInfo>> relationObjectValues;
}
