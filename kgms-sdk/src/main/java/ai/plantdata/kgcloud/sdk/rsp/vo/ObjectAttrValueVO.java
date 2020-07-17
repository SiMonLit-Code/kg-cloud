package ai.plantdata.kgcloud.sdk.rsp.vo;

import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 18:10
 * @Description:
 */
@Data
@ApiModel("边属性值查询结果模型")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class ObjectAttrValueVO {

    @ApiModelProperty(value = "实体id")
    private Long id;

    @ApiModelProperty(value = "概念id")
    private Long conceptId;
    @ApiModelProperty(value = "实体名称")
    private String name;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

    @ApiModelProperty(value = "0:概念,1:实体,2:同义词")
    private Integer type;

    @ApiModelProperty(value = "边数值属性值")
    private Map<Integer, Object> relationDataValues;

    @ApiModelProperty(value = "边对象值属性值")
    private Map<Integer, List<BasicInfoVO>> relationObjectValues;

    @ApiModelProperty(value = "关系的开始时间")
    private String attrTimeFrom;

    @ApiModelProperty(value = "关系的开始时间")
    private String attrTimeTo;

    @ApiModelProperty(value = "关系id")
    private String relationTripleId;

    @ApiModelProperty(value = "关系的metadata")
    private Map<String, Object> metaData;

    /**
     * 权重
     */
    @ApiModelProperty(value = "关系的权重")
    private Double score;

    /**
     * 来源
     */
    @ApiModelProperty(value = "关系的来源")
    private String source;
    /**
     * 可信度
     */
    @ApiModelProperty(value = "关系的可信度")
    private Double reliability;

    @ApiModelProperty(value = "关系的来源理由 ")
    private String sourceReason;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "实体批次")
    private String batch;
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
}
