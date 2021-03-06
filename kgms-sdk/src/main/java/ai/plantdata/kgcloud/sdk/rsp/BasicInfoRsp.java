package ai.plantdata.kgcloud.sdk.rsp;


import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityAttrValueVO;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityTagVO;
import ai.plantdata.kgcloud.sdk.rsp.vo.GisVO;
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
@ApiModel("概念或实体查询结果模型")
public class BasicInfoRsp extends BasicInfoVO {

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "开始时间")
    private String fromTime;

    @ApiModelProperty(value = "截止时间")
    private String toTime;

    /**
     * 权重
     */
    @ApiModelProperty(value = "实体权重")
    private Double score;

    /**
     * 来源
     */
    @ApiModelProperty(value = "实体来源")
    private String source;

    /**
     * 真实来源
     */
    @ApiModelProperty(value = "真实来源")
    private Map<String,Object> trueSource;

    /**
     * 来源操作者
     */
    @ApiModelProperty(value = "来源操作者")
    private String sourceUser;

    /**
     * 来源动作
     */
    @ApiModelProperty(value = "来源动作")
    private String sourceAction;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "实体批次")
    private String batch;

    /**
     * 可信度
     */
    @ApiModelProperty(value = "实体可信度")
    private Double reliability;

    @ApiModelProperty(value = "实体链接")
    private Set<EntityLinkVO> entityLinks;

    @ApiModelProperty(value = "实体标签")
    private List<EntityTagVO> tags;

    @ApiModelProperty(value = "gis地址")
    private GisVO gis;

    @ApiModelProperty(value = "业务额外信息")
    private Map<String, Object> additional;

    @ApiModelProperty(value = "同义词")
    private List<String> synonym;

    @ApiModelProperty(value = "父概念")
    private List<BasicInfoVO> parent;

    @ApiModelProperty(value = "子概念")
    private List<BasicInfoVO> sons;

    @ApiModelProperty(value = "实体属性值")
    private List<EntityAttrValueVO> attrValue;

    @ApiModelProperty(value = "属性分组")
    private List<GraphAttrGroupRsp> attrGroup;

    @ApiModelProperty(value = "多模态数据")
    private List<MultiModalRsp> multiModals;

}
